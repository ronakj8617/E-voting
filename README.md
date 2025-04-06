# **E-voting**

# About

Designed a simple, secure voting app for the web and Android that let users cast votes and view results in real time with confidence and ease. Focused on transparency and user trust with features like secure login, live vote syncing, and instant push notifications for updates.

# 🔧 Technologies
  - SpringBooot
  - Java
  - Android
  - ASP.NET (C#)
  - HTML/CSS
  - JavaScript
  - Firebase (Auth, real-time, DB, FCM)
  - MySQL
  - SQLite
  - Docker
  - Python
# Features
  - **Authentication**: Secure authentication using Firebase Auth
  - **Fingerpring registration**: First-time voters can register their fingerprints, which will be encrypted and stored on Firebase firestore.
  - **Voting**: Voters can cast their votes through the mobile application using a fingerprint scanner.
  - **Voter approval**: Administrators can approve users once they are verified.
  - **Elections**: Administrators can create an election ( Parliamentary, assembly, or local election).
  - **Live Data**: Data about how many votes have been cast and which party is leading will be available on the Admin Dashboard and on the app. The data is real-time.
  - **Integrity**: In case of any news of an election being compromised, admins can instantly halt an ongoing election. Voters who are declared ineligible to vote in the future but are already registered can be removed from the system as well.
  - **Vote History**: Doesn't display who a user voted but shows if they participated or not.
  - **Vote and fingerprint data**: No one can see this data, and it is only managed by the backend system to make the system impartial and secure.
