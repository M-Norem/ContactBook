### Contact Book App Overview

#### Classes and Functions Overview

**Classes:**
- **`MainActivity`**: The entry point of the application. Manages the application lifecycle, orchestrates the state of contacts, and handles SharedPreferences for data persistence.
- **`ContactRow`**: A composable function that displays an individual contact's name and phone number, along with edit and delete action buttons.
- **`ContactDialog`**: A composable dialog for adding or editing contact details, with built-in input validation.

---

**Functions:**

1. **`loadContacts()`**:
   - Retrieves the list of contacts from SharedPreferences.
   - Uses Gson to deserialize the JSON data into a list of `Contact` objects.
   - Ensures that contact data persists across app restarts.

2. **`saveContacts(contacts: List<Contact>)`**:
   - Saves the updated list of contacts to SharedPreferences.
   - Uses Gson to serialize the list of `Contact` objects into a JSON string for storage.

3. **`ContactRow(contact, onEdit, onDelete)`**:
   - Displays a single contact's details.
   - Includes "Edit" and "Delete" buttons with actions routed to the provided callbacks.

4. **`ContactDialog(initialName, initialPhone, onDismiss, onSave)`**:
   - Provides a UI for adding or editing a contact.
   - Validates input to ensure that both fields are not empty.
   - Calls the `onSave` callback with the updated name and phone number upon confirmation.

5. **`onSave(name: String, phone: String)`** (Callback in `MainActivity`):
   - Adds a new contact to the list or updates an existing contact.
   - Saves the updated list to SharedPreferences and ensures the UI is refreshed.

6. **`onEdit()` and `onDelete()`** (Callbacks in `ContactRow`):
   - `onEdit`: Opens the `ContactDialog` pre-filled with the contact's details.
   - `onDelete`: Removes the contact from the list and updates SharedPreferences.

---

#### State Variables

- **`contacts`**: A `mutableStateListOf<Contact>` that holds the current list of contacts. Automatically triggers UI updates on changes.
- **`showDialog`**: A `Boolean` that determines whether the `ContactDialog` is visible.
- **`selectedContact`**: A nullable `Contact` that tracks the currently selected contact for editing.

---
