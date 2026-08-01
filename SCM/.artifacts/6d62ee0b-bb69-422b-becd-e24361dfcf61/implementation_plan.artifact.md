# Implementation Plan - Unique Product Images

The goal is to ensure each product in the "Recommended For You" section displays its own unique image instead of a common hardcoded one.

## User Review Required

> [!IMPORTANT]
> The app currently uses a hardcoded drawable (`baground.png`) as a fallback when a product image is missing or the path is incorrect. To fix this, the backend must provide unique image paths, and the app must correctly append the base image URL.

## Proposed Changes

### Adapter Layer

#### [MODIFY] [RecommendedProductAdapter.java](file:///E:/Android/Android/SCM/app/src/main/java/com/project/scm/adaptor/RecommendedProductAdapter.java)
- Update Glide's `load()` method to use the full image path by prepending `ApiClient.IMAGE_URL` to the product's image filename.
- Add a check to handle cases where the backend might return a full URL or just a filename.

### Model Layer (Optional Research)
- Verify if `ProductResponseDTO` fields are being correctly populated from the API.

## Verification Plan

### Automated Tests
- Run `gradle_build assembleDebug` to ensure no regressions.

### Manual Verification
- Deploy the app and check the "Recommended For You" section.
- Verify that if the server provides different image filenames, Glide loads them uniquely.
- Check that the placeholder only appears during loading or on error.
