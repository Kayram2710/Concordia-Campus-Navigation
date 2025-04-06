package minicap.concordia.campusnav.firebase;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class PersistableTest {

    // Dummy implementation of Persistable for testing.
    public static class TestPersistable implements Persistable {
        private String id;
        public Map<String, Object> populatedData;

        @Override
        public String getId() {
            return id;
        }

        @Override
        public void setId(String id) {
            this.id = id;
        }

        @Override
        public void populateFieldsFromMap(Map<String, Object> data) {
            this.populatedData = data;
        }
    }

    // Mocks for Firebase Firestore components
    private MockedStatic<FirebaseFirestore> firestoreStatic;
    private FirebaseFirestore firebaseFirestoreMock;
    private CollectionReference collectionReferenceMock;
    private DocumentReference documentReferenceMock;
    private Query queryMock;

    // Each operation returns a Task; we mock them so we can chain addOnSuccessListener / addOnFailureListener
    private Task<DocumentReference> addTaskMock;
    private Task<Void> setTaskMock;
    private Task<DocumentSnapshot> getTaskMock;
    private Task<QuerySnapshot> queryTaskMock;

    @Before
    public void setUp() {
        firebaseFirestoreMock = mock(FirebaseFirestore.class);
        collectionReferenceMock = mock(CollectionReference.class);
        documentReferenceMock = mock(DocumentReference.class);
        queryMock = mock(Query.class);

        // These Task mocks need to return themselves when addOnSuccessListener / addOnFailureListener is called.
        addTaskMock = mockTask();
        setTaskMock = mockTask();
        getTaskMock = mockTask();
        queryTaskMock = mockTask();

        // Static mocking of FirebaseFirestore.getInstance()
        firestoreStatic = mockStatic(FirebaseFirestore.class);
        firestoreStatic.when(FirebaseFirestore::getInstance).thenReturn(firebaseFirestoreMock);

        // Basic stubbing to return our mocks
        when(firebaseFirestoreMock.collection(anyString())).thenReturn(collectionReferenceMock);
        when(collectionReferenceMock.document(anyString())).thenReturn(documentReferenceMock);
        when(collectionReferenceMock.add(any())).thenReturn(addTaskMock);
        when(documentReferenceMock.set(any())).thenReturn(setTaskMock);

        // For fetch-by-id
        when(documentReferenceMock.get()).thenReturn(getTaskMock);

        // For fetch-by-field
        when(collectionReferenceMock.whereEqualTo(anyString(), any())).thenReturn(queryMock);
        when(queryMock.get()).thenReturn(queryTaskMock);
    }

    @After
    public void tearDown() {
        firestoreStatic.close();
    }

    /**
     * Helper to create a Task mock that returns itself when addOnSuccessListener/addOnFailureListener is called.
     */
    @SuppressWarnings("unchecked")
    private <T> Task<T> mockTask() {
        Task<T> task = mock(Task.class);

        // Return the same mock when addOnSuccessListener(...) is called
        when(task.addOnSuccessListener(any(OnSuccessListener.class))).thenAnswer(invocation -> {
            return task;
        });

        // Return the same mock when addOnFailureListener(...) is called
        when(task.addOnFailureListener(any(OnFailureListener.class))).thenAnswer(invocation -> {
            return task;
        });

        return task;
    }

    // ------------------ Tests for save() ------------------

    @Test
    public void testSave_newDocument_success() {
        TestPersistable persistable = new TestPersistable();
        persistable.setId(null);

        // Simulate success on add -> calls onSuccess with DocumentReference
        doAnswer(invocation -> {
            OnSuccessListener<DocumentReference> listener = invocation.getArgument(0);
            when(documentReferenceMock.getId()).thenReturn("generatedId");
            listener.onSuccess(documentReferenceMock);
            return addTaskMock;
        }).when(addTaskMock).addOnSuccessListener(any());

        // Simulate success on set
        doAnswer(invocation -> {
            OnSuccessListener<Void> listener = invocation.getArgument(0);
            listener.onSuccess(null);
            return setTaskMock;
        }).when(setTaskMock).addOnSuccessListener(any());

        persistable.save();

        // After save, the id should be set
        assertEquals("generatedId", persistable.getId());
        verify(collectionReferenceMock).add(persistable);
        verify(collectionReferenceMock).document("generatedId");
        verify(documentReferenceMock).set(persistable);
    }

    @Test
    public void testSave_newDocument_failureOnAdd() {
        TestPersistable persistable = new TestPersistable();
        persistable.setId(null);

        // Simulate no success callback triggered
        doAnswer(invocation -> addTaskMock).when(addTaskMock).addOnSuccessListener(any());

        persistable.save();

        // ID remains null because onSuccess never triggered
        assertNull(persistable.getId());
        verify(collectionReferenceMock).add(persistable);
    }

    @Test
    public void testSave_existingDocument_success() {
        TestPersistable persistable = new TestPersistable();
        persistable.setId("existingId");

        // Simulate success on set
        doAnswer(invocation -> {
            OnSuccessListener<Void> listener = invocation.getArgument(0);
            listener.onSuccess(null);
            return setTaskMock;
        }).when(setTaskMock).addOnSuccessListener(any());

        persistable.save();
        verify(collectionReferenceMock).document("existingId");
        verify(documentReferenceMock).set(persistable);
    }

    @Test
    public void testSave_existingDocument_failure() {
        TestPersistable persistable = new TestPersistable();
        persistable.setId("existingId");

        // Simulate no success callback triggered
        doAnswer(invocation -> setTaskMock).when(setTaskMock).addOnSuccessListener(any());

        persistable.save();
        verify(collectionReferenceMock).document("existingId");
        verify(documentReferenceMock).set(persistable);
    }

    // ------------------ Tests for fetch(OnSuccessListener<Void>) ------------------

    @Test
    public void testFetch_byId_nullId() {
        TestPersistable persistable = new TestPersistable();
        persistable.setId(null);

        OnSuccessListener<Void> successListener = mock(OnSuccessListener.class);
        persistable.fetch(successListener);

        // Should not call onSuccess
        verifyNoInteractions(successListener);
    }

    @Test
    public void testFetch_byId_success_documentExists() {
        TestPersistable persistable = new TestPersistable();
        persistable.setId("docId");

        // Mock documentSnapshot
        DocumentSnapshot documentSnapshot = mock(DocumentSnapshot.class);
        when(documentSnapshot.exists()).thenReturn(true);
        Map<String, Object> data = new HashMap<>();
        data.put("key", "value");
        when(documentSnapshot.getData()).thenReturn(data);

        // Simulate success
        doAnswer(invocation -> {
            OnSuccessListener<DocumentSnapshot> listener = invocation.getArgument(0);
            listener.onSuccess(documentSnapshot);
            return getTaskMock; // Return the same mock
        }).when(getTaskMock).addOnSuccessListener(any());

        OnSuccessListener<Void> successListener = mock(OnSuccessListener.class);
        persistable.fetch(successListener);

        // Confirm fields got populated
        assertEquals(data, persistable.populatedData);
        verify(successListener).onSuccess(null);
    }

    @Test
    public void testFetch_byId_documentDoesNotExist() {
        TestPersistable persistable = new TestPersistable();
        persistable.setId("docId");

        DocumentSnapshot documentSnapshot = mock(DocumentSnapshot.class);
        when(documentSnapshot.exists()).thenReturn(false);

        // Simulate success with non-existent doc
        doAnswer(invocation -> {
            OnSuccessListener<DocumentSnapshot> listener = invocation.getArgument(0);
            listener.onSuccess(documentSnapshot);
            return getTaskMock;
        }).when(getTaskMock).addOnSuccessListener(any());

        OnSuccessListener<Void> successListener = mock(OnSuccessListener.class);
        persistable.fetch(successListener);

        // onSuccess should not be called since document doesn't exist
        verifyNoInteractions(successListener);
    }

    @Test
    public void testFetch_byId_failure() {
        TestPersistable persistable = new TestPersistable();
        persistable.setId("docId");

        // Simulate no success callback
        doAnswer(invocation -> getTaskMock).when(getTaskMock).addOnSuccessListener(any());
        // Optionally trigger the onFailure callback if needed, but we’re just verifying that onSuccess is never called.

        OnSuccessListener<Void> successListener = mock(OnSuccessListener.class);
        persistable.fetch(successListener);

        verifyNoInteractions(successListener);
    }

    // ------------------ Tests for fetch(String, Object, OnSuccessListener<Void>) ------------------

    @Test
    public void testFetch_byField_success() {
        TestPersistable persistable = new TestPersistable();

        // Mock documentSnapshot
        DocumentSnapshot documentSnapshot = mock(DocumentSnapshot.class);
        when(documentSnapshot.getId()).thenReturn("queriedId");
        Map<String, Object> data = Collections.singletonMap("field", "value");
        when(documentSnapshot.getData()).thenReturn(data);

        // Mock query result
        QuerySnapshot querySnapshot = mock(QuerySnapshot.class);
        when(querySnapshot.isEmpty()).thenReturn(false);
        when(querySnapshot.getDocuments()).thenReturn(Collections.singletonList(documentSnapshot));

        // Simulate success
        doAnswer(invocation -> {
            OnSuccessListener<QuerySnapshot> listener = invocation.getArgument(0);
            listener.onSuccess(querySnapshot);
            return queryTaskMock;
        }).when(queryTaskMock).addOnSuccessListener(any());

        OnSuccessListener<Void> successListener = mock(OnSuccessListener.class);
        persistable.fetch("field", "value", successListener);

        assertEquals("queriedId", persistable.getId());
        assertEquals(data, persistable.populatedData);
        verify(successListener).onSuccess(null);
    }

    @Test
    public void testFetch_byField_emptyQuery() {
        TestPersistable persistable = new TestPersistable();

        QuerySnapshot querySnapshot = mock(QuerySnapshot.class);
        when(querySnapshot.isEmpty()).thenReturn(true);

        // Simulate success with empty result
        doAnswer(invocation -> {
            OnSuccessListener<QuerySnapshot> listener = invocation.getArgument(0);
            listener.onSuccess(querySnapshot);
            return queryTaskMock;
        }).when(queryTaskMock).addOnSuccessListener(any());

        OnSuccessListener<Void> successListener = mock(OnSuccessListener.class);
        persistable.fetch("field", "value", successListener);

        // No document found -> no onSuccess
        assertNull(persistable.getId());
        verifyNoInteractions(successListener);
    }

    @Test
    public void testFetch_byField_failure() {
        TestPersistable persistable = new TestPersistable();

        // Simulate no success callback
        doAnswer(invocation -> queryTaskMock).when(queryTaskMock).addOnSuccessListener(any());

        OnSuccessListener<Void> successListener = mock(OnSuccessListener.class);
        persistable.fetch("field", "value", successListener);

        verifyNoInteractions(successListener);
    }
}
