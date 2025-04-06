package minicap.concordia.campusnav.firebase;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnSuccessListener;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Dummy concrete implementation of Persistable for testing.
class DummyPersistable implements Persistable {
    private String id;
    private Map<String, Object> data; // To store fetched data.

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
        this.data = data;
    }

    public Map<String, Object> getData() {
        return data;
    }
}

@RunWith(RobolectricTestRunner.class)
public class PersistableTest {

    private DummyPersistable dummy;

    @Before
    public void setUp() {
        dummy = new DummyPersistable();
    }

    @Test
    public void testSaveCreatesDocumentWhenIdIsNull() {
        // Arrange: ensure dummy has no ID.
        assertNull(dummy.getId());
        String collectionName = dummy.getClass().getSimpleName();

        // Create mocks for Firestore chain.
        FirebaseFirestore mockFirestore = mock(FirebaseFirestore.class);
        CollectionReference mockCollection = mock(CollectionReference.class);
        DocumentReference mockDocRef = mock(DocumentReference.class);
        DocumentReference mockDocRef2 = mock(DocumentReference.class);

        try (MockedStatic<FirebaseFirestore> firestoreStatic = mockStatic(FirebaseFirestore.class)) {
            firestoreStatic.when(FirebaseFirestore::getInstance).thenReturn(mockFirestore);
            when(mockFirestore.collection(collectionName)).thenReturn(mockCollection);
            // Simulate add() returns a successful Task with mockDocRef.
            when(mockCollection.add(any())).thenReturn(Tasks.forResult(mockDocRef));
            when(mockDocRef.getId()).thenReturn("newId");
            // When calling document("newId") return mockDocRef2 and simulate set() returning successful task.
            when(mockCollection.document("newId")).thenReturn(mockDocRef2);
            when(mockDocRef2.set(any())).thenReturn(Tasks.forResult(null));

            // Act
            dummy.save();

            // Assert: the dummy's id should now be set.
            assertEquals("newId", dummy.getId());
        }
    }

    @Test
    public void testSaveUpdatesDocumentWhenIdIsNotNull() {
        // Arrange: set an existing ID.
        dummy.setId("existingId");
        String collectionName = dummy.getClass().getSimpleName();

        FirebaseFirestore mockFirestore = mock(FirebaseFirestore.class);
        CollectionReference mockCollection = mock(CollectionReference.class);
        DocumentReference mockDocRef = mock(DocumentReference.class);

        try (MockedStatic<FirebaseFirestore> firestoreStatic = mockStatic(FirebaseFirestore.class)) {
            firestoreStatic.when(FirebaseFirestore::getInstance).thenReturn(mockFirestore);
            when(mockFirestore.collection(collectionName)).thenReturn(mockCollection);
            when(mockCollection.document("existingId")).thenReturn(mockDocRef);
            when(mockDocRef.set(any())).thenReturn(Tasks.forResult(null));

            // Act
            dummy.save();

            // Assert: the id remains unchanged.
            assertEquals("existingId", dummy.getId());
        }
    }

    @Test
    public void testFetchWithoutIdDoesNothing() {
        // Arrange: dummy has no ID.
        assertNull(dummy.getId());
        final boolean[] called = {false};
        OnSuccessListener<Void> listener = aVoid -> called[0] = true;

        // Act
        dummy.fetch(listener);

        // Assert: listener is not called.
        assertFalse(called[0]);
    }

    @Test
    public void testFetchWithIdAndDocumentExists() {
        // Arrange: set an ID.
        dummy.setId("existingId");
        String collectionName = dummy.getClass().getSimpleName();

        FirebaseFirestore mockFirestore = mock(FirebaseFirestore.class);
        CollectionReference mockCollection = mock(CollectionReference.class);
        DocumentReference mockDocRef = mock(DocumentReference.class);
        DocumentSnapshot mockDocSnapshot = mock(DocumentSnapshot.class);
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("key", "value");

        try (MockedStatic<FirebaseFirestore> firestoreStatic = mockStatic(FirebaseFirestore.class)) {
            firestoreStatic.when(FirebaseFirestore::getInstance).thenReturn(mockFirestore);
            when(mockFirestore.collection(collectionName)).thenReturn(mockCollection);
            when(mockCollection.document("existingId")).thenReturn(mockDocRef);
            // Simulate get() returns a successful Task with a DocumentSnapshot.
            when(mockDocRef.get()).thenReturn(Tasks.forResult(mockDocSnapshot));
            when(mockDocSnapshot.exists()).thenReturn(true);
            when(mockDocSnapshot.getData()).thenReturn(dataMap);

            final boolean[] called = {false};
            OnSuccessListener<Void> listener = aVoid -> called[0] = true;

            // Act
            dummy.fetch(listener);

            // Assert: dummy's populateFieldsFromMap should have been called.
            assertEquals(dataMap, dummy.getData());
            assertTrue(called[0]);
        }
    }

    @Test
    public void testFetchByFieldWithResult() {
        // Arrange
        String collectionName = dummy.getClass().getSimpleName();
        FirebaseFirestore mockFirestore = mock(FirebaseFirestore.class);
        CollectionReference mockCollection = mock(CollectionReference.class);
        Query mockQuery = mock(Query.class);
        QuerySnapshot mockQuerySnapshot = mock(QuerySnapshot.class);
        DocumentSnapshot mockDocSnapshot = mock(DocumentSnapshot.class);
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("key", "value");

        try (MockedStatic<FirebaseFirestore> firestoreStatic = mockStatic(FirebaseFirestore.class)) {
            firestoreStatic.when(FirebaseFirestore::getInstance).thenReturn(mockFirestore);
            when(mockFirestore.collection(collectionName)).thenReturn(mockCollection);
            when(mockCollection.whereEqualTo("field", "value")).thenReturn(mockQuery);
            when(mockQuery.get()).thenReturn(Tasks.forResult(mockQuerySnapshot));
            when(mockQuerySnapshot.isEmpty()).thenReturn(false);
            List<DocumentSnapshot> documents = new ArrayList<>();
            documents.add(mockDocSnapshot);
            when(mockQuerySnapshot.getDocuments()).thenReturn(documents);
            when(mockDocSnapshot.getId()).thenReturn("queriedId");
            when(mockDocSnapshot.getData()).thenReturn(dataMap);

            final boolean[] called = {false};
            OnSuccessListener<Void> listener = aVoid -> called[0] = true;

            // Act
            dummy.fetch("field", "value", listener);

            // Assert: the dummy's id should be set from the query and data populated.
            assertEquals("queriedId", dummy.getId());
            assertEquals(dataMap, dummy.getData());
            assertTrue(called[0]);
        }
    }

    @Test
    public void testFetchByFieldWithNoResult() {
        // Arrange
        String collectionName = dummy.getClass().getSimpleName();
        FirebaseFirestore mockFirestore = mock(FirebaseFirestore.class);
        CollectionReference mockCollection = mock(CollectionReference.class);
        Query mockQuery = mock(Query.class);
        QuerySnapshot mockQuerySnapshot = mock(QuerySnapshot.class);

        try (MockedStatic<FirebaseFirestore> firestoreStatic = mockStatic(FirebaseFirestore.class)) {
            firestoreStatic.when(FirebaseFirestore::getInstance).thenReturn(mockFirestore);
            when(mockFirestore.collection(collectionName)).thenReturn(mockCollection);
            when(mockCollection.whereEqualTo("field", "value")).thenReturn(mockQuery);
            when(mockQuery.get()).thenReturn(Tasks.forResult(mockQuerySnapshot));
            when(mockQuerySnapshot.isEmpty()).thenReturn(true);

            final boolean[] called = {false};
            OnSuccessListener<Void> listener = aVoid -> called[0] = true;

            // Act
            dummy.fetch("field", "value", listener);

            // Assert: the listener should not be called when the query returns empty.
            assertFalse(called[0]);
        }
    }
}
