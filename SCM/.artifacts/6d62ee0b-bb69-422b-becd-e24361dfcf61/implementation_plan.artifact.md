# Implementation Plan - Dynamic Milestone Pipeline & Vertical Timeline

Make the "MILESTONE PROGRESS PIPELINE" and "ORDER PROCESSING UPDATE" sections in `TrackingDashboardActivity` fully dynamic based on the order status.

## Proposed Changes

### Layout Layer

#### [MODIFY] [activity_tracking_dashboard.xml](file:///E:/Android/Android/SCM/app/src/main/res/layout/activity_tracking_dashboard.xml)
- **Horizontal Stepper**:
    - Add a Pin/Indicator icon (`ivMilestonePointer`) above the labels.
    - Use `ConstraintLayout` to position the pointer dynamically using `bias`.
- **Vertical Timeline**:
    - Replace the static "Timeline Item 1" with a `LinearLayout` (`containerTimeline`) to add steps dynamically.
    - Remove the hardcoded placeholder text.

#### [NEW] [item_timeline_step.xml](file:///E:/Android/Android/SCM/app/src/main/res/layout/item_timeline_step.xml)
- Create a template layout for a single vertical timeline step.
- Includes: Icon, Title, Description, Date/Time, and a vertical line for continuity.

### Activity Layer

#### [MODIFY] [TrackingDashboardActivity.java](file:///E:/Android/Android/SCM/app/src/main/java/com/project/scm/TrackingDashboardActivity.java)
- **Status Mapping Logic**:
    - Define an internal list of milestones: `PENDING`, `CONFIRMED`, `PROCESSING`, `SHIPPED`, `DELIVERED`.
- **Horizontal Update**:
    - Implement `updateMilestonePointer(String status)` to move the pin to the correct horizontal position (0% to 100% bias).
- **Vertical Update**:
    - Implement `populateVerticalTimeline(String currentStatus, String date)` to clear the container and add all completed and current steps.
    - Style active/completed steps in green and future steps in gray.

## Verification Plan

### Automated Tests
- Run `gradle_build assembleDebug` to ensure layout changes don't break the build.

### Manual Verification
- Track an order with status `SHIPPED`.
- Verify the horizontal pin is over the "Shipped" label.
- Verify the vertical timeline shows:
    1. Order Placed (Green/Completed)
    2. Order Confirmed (Green/Completed)
    3. Processing (Green/Completed)
    4. Shipped (Green/Active)
- Track an order with status `PENDING`.
- Verify only "Order Placed" shows in the vertical timeline and the horizontal pin is at the start.
