# Play Store production checklist

- Choose a permanent application ID before the first upload.
- Replace the support-email placeholder in the privacy policy.
- Create a private upload keystore; never commit it.
- Set the four `DU_KEYSTORE_*` environment variables described in README.
- Run `gradle clean testReleaseUnitTest lintRelease bundleRelease`.
- Upload the generated `.aab` from `app/build/outputs/bundle/release/`.
- Complete Data safety (no data collected), content rating, app access, ads declaration and target-audience forms truthfully.
- Supply icon, feature graphic, phone/tablet screenshots, support email and hosted privacy-policy URL.
- Test through Play Console internal testing before production rollout.
- Retain the upload key and increase `versionCode` for every release.

This package is production-oriented source, but Google Play publication still requires the publisher's identity, signing key, listing assets and Play Console review.
