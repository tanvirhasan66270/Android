# Implementation Plan - Convert Project to Pure Java

The goal is to convert all remaining Kotlin files in the project to Java to maintain a consistent single-language codebase.

## Proposed Changes

### Activities (Kotlin to Java Conversion)

#### [NEW] [OrderDashboardActivity.java](file:///E:/Android/Android/SCM/app/src/main/java/com/project/scm/OrderDashboardActivity.java)
- Port logic from `OrderDashboardActivity.kt`.
- Implement `EdgeToEdge` and `WindowInsetsListener` in Java.
- Setup button listeners and navigation.

#### [NEW] [TrackingDashboardActivity.java](file:///E:/Android/Android/SCM/app/src/main/java/com/project/scm/TrackingDashboardActivity.java)
- Port logic from `TrackingDashboardActivity.kt`.
- Implement navigation and UI setup in Java.

#### [NEW] [BillingPage.java](file:///E:/Android/Android/SCM/app/src/main/java/com/project/scm/BillingPage.java)
- Port logic from `BillingPage.kt`.

#### [NEW] [SupportDesk.java](file:///E:/Android/Android/SCM/app/src/main/java/com/project/scm/SupportDesk.java)
- Port logic from `SupportDesk.kt`.
- Implement `TextWatcher` for the character counter in Java.

### Cleanup

#### [DELETE] All `.kt` files
- Remove `OrderDashboardActivity.kt`, `TrackingDashboardActivity.kt`, `BillingPage.kt`, and `SupportDesk.kt`.

#### [MODIFY] [build.gradle](file:///E:/Android/Android/SCM/app/build.gradle)
- (Optional) Remove Kotlin-specific plugins and dependencies if no longer needed.

## Verification Plan

### Automated Tests
- Run `gradle_build assembleDebug` to ensure the project compiles successfully after conversion.

### Manual Verification
- Verify navigation between all screens (Dashboard -> Orders -> Tracking -> Billing -> Support).
- Confirm character counting in Support Desk works.
- Check that edge-to-edge layout remains correct.
