# Implementation Plan - Support Desk

The goal is to implement the "Support Desk" screen based on the provided design. This screen allows users to submit support tickets, view priority levels, and access immediate help channels (Call, Chat, Email).

## Proposed Changes

### Resources

#### [MODIFY] [colors.xml](file:///E:/Android/Android/SCM/app/src/main/res/values/colors.xml)
- Add colors for header gradient and required field indicators.

#### [NEW] [Drawables]
- `ic_headset.xml`: Support icon for the header.
- `ic_send.xml`: Plane icon for the submit button.
- `ic_call.xml`, `ic_chat.xml`, `ic_email.xml`: Icons for help channels.
- `ic_flag.xml`: Icon for priority level.
- `bg_support_header.xml`: Blue gradient background for the header.

### Layouts

#### [MODIFY] [activity_support_desk.xml](file:///E:/Android/Android/SCM/app/src/main/res/layout/activity_support_desk.xml)
- **Header**: Custom blue gradient area with a back button, headset icon, "Support Desk" title, notification badge, and profile initial.
- **Scroll Container**: `NestedScrollView` containing the main form.
- **Response Time Info**: A rounded card with an info icon explaining typical response times.
- **Ticket Form**:
    - **Issue Subject**: Input field with a required asterisk.
    - **Related Order**: Optional order ID input field.
    - **Priority Level**: Dropdown selection (MEDIUM/HIGH/LOW).
    - **Issue Description**: Multi-line text area with a character counter.
- **Immediate Help Section**: "Need Immediate Help?" horizontal cards for quick contact.
- **Security Notice**: A card with a lock icon confirming data encryption.
- **Actions**: "Submit Ticket" (Primary) and "Cancel" (Secondary) buttons.
- **Bottom Navigation**: Standard bottom navigation bar with "Dashboard" as active or Profile if applicable.

### Code

#### [MODIFY] [SupportDesk.kt](file:///E:/Android/Android/SCM/app/src/main/java/com/project/scm/SupportDesk.kt)
- Setup edge-to-edge rendering.
- Initialize navigation for the back button.
- Setup basic character counting logic for the description field.

#### [MODIFY] [Dashboard_Activity.java](file:///E:/Android/Android/SCM/app/src/main/java/com/project/scm/Dashboard_Activity.java)
- Link the "Support Desk" grid item to the `SupportDesk` activity.

## Verification Plan

### Manual Verification
- Deploy the app and navigate to the Support Desk from the Dashboard.
- Visually verify that the header gradient and icons match the design.
- Check that the form is scrollable and inputs are interactive.
- Verify the back button returns the user to the Dashboard.
