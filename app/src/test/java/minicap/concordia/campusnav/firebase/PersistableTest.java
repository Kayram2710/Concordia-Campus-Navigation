package minicap.concordia.campusnav.firebase;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Test;
import org.mockito.MockedStatic;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Expanded test class to achieve 100% coverage of Persistable's default methods.
 */
public class PersistableTest {

    // ---------------------------------------------------------------------
    // 1) Dummy implementation of Persistable for testing
    // ---------------------------------------------------------------------
    public static class DummyPersistable implements Persistable {
        private String id;
        private String field;

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
            if (data != null && data.containsKey("field")) {
                this.field = (String) data.get("field");
            }
        }

        public String getField() {
            return field;
        }
    }

    // ---------------------------------------------------------------------
    // 2) Existing "happy path" tests for save() and fetch()
    // ---------------------------------------------------------------------

    @Test
    public void testSaveCreatesNewDocument() throws Exception {
        DummyPersistable dummy = new DummyPersistable(); // no ID => triggers "create" path

        FirebaseFirestore mockFirestore = mock(FirebaseFirestore.class);
        CollectionReference mockCollection = mock(CollectionReference.class);
        DocumentReference mockDocRef = mock(DocumentReference.class);

        try (MockedStatic<FirebaseFirestore> firestoreStatic = mockStatic(FirebaseFirestore.class)) {
            firestoreStatic.when(FirebaseFirestore::getInstance).thenReturn(mockFirestore);
            when(mockFirestore.collection(dummy.getClass().getSimpleName())).thenReturn(mockCollection);

            // Simulate add() success
            Task<DocumentReference> addTask = Tasks.forResult(mockDocRef);
            when(mockCollection.add(any(DummyPersistable.class))).thenReturn(addTask);

            // docRef ID => "dummy_id"
            when(mockDocRef.getId()).thenReturn("dummy_id");

            // Then set() success
            Task<Void> setTask = Tasks.forResult(null);
            when(mockCollection.document("dummy_id")).thenReturn(mockDocRef);
            when(mockDocRef.set(any(DummyPersistable.class))).thenReturn(setTask);

            // Act
            dummy.save();

            // Assert
            assertEquals("dummy_id", dummy.getId());
            verify(mockDocRef, times(1)).set(dummy);
        }
    }

    @Test
    public void testSaveUpdatesExistingDocument() throws Exception {
        DummyPersistable dummy = new DummyPersistable();
        dummy.setId("existing_id"); // triggers "update" path

        FirebaseFirestore mockFirestore = mock(FirebaseFirestore.class);
        CollectionReference mockCollection = mock(CollectionReference.class);
        DocumentReference mockDocRef = mock(DocumentReference.class);

        try (MockedStatic<FirebaseFirestore> firestoreStatic = mockStatic(FirebaseFirestore.class)) {
            firestoreStatic.when(FirebaseFirestore::getInstance).thenReturn(mockFirestore);
            when(mockFirestore.collection(dummy.getClass().getSimpleName())).thenReturn(mockCollection);
            when(mockCollection.document("existing_id")).thenReturn(mockDocRef);

            // set() success
            Task<Void> setTask = Tasks.forResult(null);
            when(mockDocRef.set(any(DummyPersistable.class))).thenReturn(setTask);

            // Act
            dummy.save();

            // Assert
            verify(mockDocRef, times(1)).set(dummy);
        }
    }

    @Test
    public void testFetchByIdSuccess() throws Exception {
        DummyPersistable dummy = new DummyPersistable();
        dummy.setId("existing_id");

        FirebaseFirestore mockFirestore = mock(FirebaseFirestore.class);
        CollectionReference mockCollection = mock(CollectionReference.class);
        DocumentReference mockDocRef = mock(DocumentReference.class);

        // Fake doc snapshot
        DocumentSnapshot mockSnapshot = mock(DocumentSnapshot.class);
        Map<String, Object> fakeData = new HashMap<>();
        fakeData.put("field", "test_value");
        when(mockSnapshot.exists()).thenReturn(true);
        when(mockSnapshot.getData()).thenReturn(fakeData);

        // get() success
        Task<DocumentSnapshot> getTask = Tasks.forResult(mockSnapshot);

        try (MockedStatic<FirebaseFirestore> firestoreStatic = mockStatic(FirebaseFirestore.class)) {
            firestoreStatic.when(FirebaseFirestore::getInstance).thenReturn(mockFirestore);
            when(mockFirestore.collection(dummy.getClass().getSimpleName())).thenReturn(mockCollection);
            when(mockCollection.document("existing_id")).thenReturn(mockDocRef);
            when(mockDocRef.get()).thenReturn(getTask);

            final boolean[] successCalled = { false };
            dummy.fetch(aVoid -> successCalled[0] = true);

            assertEquals("test_value", dummy.getField());
            assertTrue("onSuccessListener should be called", successCalled[0]);
        }
    }

    @Test
    public void testFetchByFieldSuccess() throws Exception {
        DummyPersistable dummy = new DummyPersistable();

        FirebaseFirestore mockFirestore = mock(FirebaseFirestore.class);
        CollectionReference mockCollection = mock(CollectionReference.class);

        // Fake doc snapshot
        DocumentSnapshot mockSnapshot = mock(DocumentSnapshot.class);
        Map<String, Object> fakeData = new HashMap<>();
        fakeData.put("field", "field_value");
        when(mockSnapshot.exists()).thenReturn(true);
        when(mockSnapshot.getData()).thenReturn(fakeData);
        when(mockSnapshot.getId()).thenReturn("new_id");

        // Fake query snapshot
        QuerySnapshot mockQuerySnapshot = mock(QuerySnapshot.class);
        when(mockQuerySnapshot.isEmpty()).thenReturn(false);
        when(mockQuerySnapshot.getDocuments()).thenReturn(Collections.singletonList(mockSnapshot));
        Task<QuerySnapshot> queryTask = Tasks.forResult(mockQuerySnapshot);

        try (MockedStatic<FirebaseFirestore> firestoreStatic = mockStatic(FirebaseFirestore.class)) {
            firestoreStatic.when(FirebaseFirestore::getInstance).thenReturn(mockFirestore);
            when(mockFirestore.collection(dummy.getClass().getSimpleName())).thenReturn(mockCollection);

            // Return same mockCollection for chaining
            when(mockCollection.whereEqualTo("field", "value")).thenReturn(mockCollection);
            when(mockCollection.get()).thenReturn(queryTask);

            final boolean[] successCalled = { false };
            dummy.fetch("field", "value", aVoid -> successCalled[0] = true);

            assertEquals("new_id", dummy.getId());
            assertEquals("field_value", dummy.getField());
            assertTrue("onSuccessListener should be called", successCalled[0]);
        }
    }

    // ---------------------------------------------------------------------
    // 3) Additional tests for error branches => 100% coverage
    // ---------------------------------------------------------------------

    // (A) Save => "create" path => add() fails
    @Test
    public void testSaveCreateFails() throws Exception {
        DummyPersistable dummy = new DummyPersistable(); // no ID => create path

        FirebaseFirestore mockFirestore = mock(FirebaseFirestore.class);
        CollectionReference mockCollection = mock(CollectionReference.class);

        // Force add() to fail with an exception
        Task<DocumentReference> failedAddTask = Tasks.forException(new Exception("Simulated add() failure"));

        try (MockedStatic<FirebaseFirestore> firestoreStatic = mockStatic(FirebaseFirestore.class)) {
            firestoreStatic.when(FirebaseFirestore::getInstance).thenReturn(mockFirestore);
            when(mockFirestore.collection(dummy.getClass().getSimpleName())).thenReturn(mockCollection);
            when(mockCollection.add(any(DummyPersistable.class))).thenReturn(failedAddTask);

            // Act
            dummy.save();

            // There's no direct exception thrown from save(), but we covered the onFailure path in logs.
            // Just ensure we got no ID assigned.
            assertNull("ID should remain null if add() fails", dummy.getId());
        }
    }

    // (B) Save => "create" path => add() success, but set() fails
    @Test
    public void testSaveCreateSetFails() throws Exception {
        DummyPersistable dummy = new DummyPersistable(); // triggers create

        FirebaseFirestore mockFirestore = mock(FirebaseFirestore.class);
        CollectionReference mockCollection = mock(CollectionReference.class);
        DocumentReference mockDocRef = mock(DocumentReference.class);

        // add() success => docRef
        Task<DocumentReference> addTask = Tasks.forResult(mockDocRef);
        // docRef => "some_id"
        when(mockDocRef.getId()).thenReturn("some_id");

        // set() fails
        Task<Void> failedSetTask = Tasks.forException(new Exception("Simulated set() failure"));

        try (MockedStatic<FirebaseFirestore> firestoreStatic = mockStatic(FirebaseFirestore.class)) {
            firestoreStatic.when(FirebaseFirestore::getInstance).thenReturn(mockFirestore);
            when(mockFirestore.collection(dummy.getClass().getSimpleName())).thenReturn(mockCollection);

            when(mockCollection.add(any(DummyPersistable.class))).thenReturn(addTask);
            when(mockCollection.document("some_id")).thenReturn(mockDocRef);
            when(mockDocRef.set(any(DummyPersistable.class))).thenReturn(failedSetTask);

            // Act
            dummy.save();

            // ID should have been set from docRef, even though set() failed
            assertEquals("some_id", dummy.getId());
        }
    }

    // (C) Save => "update" path => set() fails
    @Test
    public void testSaveUpdateFails() throws Exception {
        DummyPersistable dummy = new DummyPersistable();
        dummy.setId("already_id"); // triggers update

        FirebaseFirestore mockFirestore = mock(FirebaseFirestore.class);
        CollectionReference mockCollection = mock(CollectionReference.class);
        DocumentReference mockDocRef = mock(DocumentReference.class);

        // set() fails
        Task<Void> failedSetTask = Tasks.forException(new Exception("Simulated set() failure"));

        try (MockedStatic<FirebaseFirestore> firestoreStatic = mockStatic(FirebaseFirestore.class)) {
            firestoreStatic.when(FirebaseFirestore::getInstance).thenReturn(mockFirestore);
            when(mockFirestore.collection(dummy.getClass().getSimpleName())).thenReturn(mockCollection);
            when(mockCollection.document("already_id")).thenReturn(mockDocRef);
            when(mockDocRef.set(dummy)).thenReturn(failedSetTask);

            // Act
            dummy.save();

            // The code doesn't throw, but we covered the onFailure branch in logs.
            // ID remains "already_id"
            assertEquals("already_id", dummy.getId());
        }
    }

    // (D) fetch(OnSuccessListener) => ID is null => logs error & returns immediately
    @Test
    public void testFetchNoId() throws Exception {
        DummyPersistable dummy = new DummyPersistable(); // no ID => immediate return

        // If there's no ID, fetch() won't even call Firestore.
        // We can mock Firestore but it's never used.

        final boolean[] successCalled = { false };
        dummy.fetch(aVoid -> successCalled[0] = true);

        // onSuccess should not be called
        assertFalse("onSuccessListener should NOT be called if ID is null", successCalled[0]);
    }

    // (E) fetch(OnSuccessListener) => doc does NOT exist
    @Test
    public void testFetchDocDoesNotExist() throws Exception {
        DummyPersistable dummy = new DummyPersistable();
        dummy.setId("some_id");

        FirebaseFirestore mockFirestore = mock(FirebaseFirestore.class);
        CollectionReference mockCollection = mock(CollectionReference.class);
        DocumentReference mockDocRef = mock(DocumentReference.class);

        // doc snapshot that "doesn't exist"
        DocumentSnapshot mockSnapshot = mock(DocumentSnapshot.class);
        when(mockSnapshot.exists()).thenReturn(false);

        Task<DocumentSnapshot> getTask = Tasks.forResult(mockSnapshot);

        try (MockedStatic<FirebaseFirestore> firestoreStatic = mockStatic(FirebaseFirestore.class)) {
            firestoreStatic.when(FirebaseFirestore::getInstance).thenReturn(mockFirestore);
            when(mockFirestore.collection(dummy.getClass().getSimpleName())).thenReturn(mockCollection);
            when(mockCollection.document("some_id")).thenReturn(mockDocRef);
            when(mockDocRef.get()).thenReturn(getTask);

            final boolean[] successCalled = { false };
            dummy.fetch(aVoid -> successCalled[0] = true);

            // doc doesn't exist => we log error and do not call onSuccess
            assertFalse("onSuccessListener not called if doc does not exist", successCalled[0]);
        }
    }

    // (F) fetch(OnSuccessListener) => doc get fails
    @Test
    public void testFetchDocGetFails() throws Exception {
        DummyPersistable dummy = new DummyPersistable();
        dummy.setId("some_id");

        FirebaseFirestore mockFirestore = mock(FirebaseFirestore.class);
        CollectionReference mockCollection = mock(CollectionReference.class);
        DocumentReference mockDocRef = mock(DocumentReference.class);

        // get() fails
        Task<DocumentSnapshot> failedGetTask = Tasks.forException(new Exception("Simulated get() failure"));

        try (MockedStatic<FirebaseFirestore> firestoreStatic = mockStatic(FirebaseFirestore.class)) {
            firestoreStatic.when(FirebaseFirestore::getInstance).thenReturn(mockFirestore);
            when(mockFirestore.collection(dummy.getClass().getSimpleName())).thenReturn(mockCollection);
            when(mockCollection.document("some_id")).thenReturn(mockDocRef);
            when(mockDocRef.get()).thenReturn(failedGetTask);

            final boolean[] successCalled = { false };
            dummy.fetch(aVoid -> successCalled[0] = true);

            // fetch fails => we do not call onSuccess
            assertFalse("onSuccessListener not called if doc get fails", successCalled[0]);
        }
    }

    // (G) fetch(String fieldName, Object fieldValue, OnSuccessListener) => no documents found
    @Test
    public void testFetchByFieldNoResults() throws Exception {
        DummyPersistable dummy = new DummyPersistable();

        FirebaseFirestore mockFirestore = mock(FirebaseFirestore.class);
        CollectionReference mockCollection = mock(CollectionReference.class);

        // empty query snapshot
        QuerySnapshot mockQuerySnapshot = mock(QuerySnapshot.class);
        when(mockQuerySnapshot.isEmpty()).thenReturn(true);

        Task<QuerySnapshot> queryTask = Tasks.forResult(mockQuerySnapshot);

        try (MockedStatic<FirebaseFirestore> firestoreStatic = mockStatic(FirebaseFirestore.class)) {
            firestoreStatic.when(FirebaseFirestore::getInstance).thenReturn(mockFirestore);
            when(mockFirestore.collection(dummy.getClass().getSimpleName())).thenReturn(mockCollection);

            when(mockCollection.whereEqualTo("field", "value")).thenReturn(mockCollection);
            when(mockCollection.get()).thenReturn(queryTask);

            final boolean[] successCalled = { false };
            dummy.fetch("field", "value", aVoid -> successCalled[0] = true);

            // No documents => we do not call onSuccess
            assertFalse("onSuccessListener not called if no documents found", successCalled[0]);
            assertNull("ID remains null if no documents found", dummy.getId());
        }
    }

    // (H) fetch(String fieldName, Object fieldValue, OnSuccessListener) => query fails
    @Test
    public void testFetchByFieldFails() throws Exception {
        DummyPersistable dummy = new DummyPersistable();

        FirebaseFirestore mockFirestore = mock(FirebaseFirestore.class);
        CollectionReference mockCollection = mock(CollectionReference.class);

        // query fails
        Task<QuerySnapshot> failedQueryTask = Tasks.forException(new Exception("Simulated query failure"));

        try (MockedStatic<FirebaseFirestore> firestoreStatic = mockStatic(FirebaseFirestore.class)) {
            firestoreStatic.when(FirebaseFirestore::getInstance).thenReturn(mockFirestore);
            when(mockFirestore.collection(dummy.getClass().getSimpleName())).thenReturn(mockCollection);

            when(mockCollection.whereEqualTo("field", "value")).thenReturn(mockCollection);
            when(mockCollection.get()).thenReturn(failedQueryTask);

            final boolean[] successCalled = { false };
            dummy.fetch("field", "value", aVoid -> successCalled[0] = true);

            // query fails => onSuccess not called
            assertFalse("onSuccessListener not called if query fails", successCalled[0]);
            assertNull("ID remains null if query fails", dummy.getId());
        }
    }
}
