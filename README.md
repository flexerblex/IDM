# Identity Management Application

CapID allows users to have their data protected with multiple forms of identification. It gives users a platform to authenticate and authorize, providing secure access to applications, devices, and users.

This Identity Management system supports the following methods of authentication:

* Fingerprint
* Facial Recognition
* Voice Recognition 

This application implements [OAuth 2.0](https://oauth.net/2/native-apps/) functionality to allow users to access third party applications. The [Instagram Basic Display API](https://developers.facebook.com/docs/instagram-basic-display-api/) is integrated with this project to use with our authorization server to allow users to be able to view their posts on instagram. 

## Project setup

In order to build this project, you need to download and install [Android Studio](https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=&ved=2ahUKEwjqyaCMiNLtAhWRv1kKHb32CJgQFjAAegQIARAD&url=https%3A%2F%2Fdeveloper.android.com%2Fstudio&usg=AOvVaw3fIlahucURgOEYHHhVdQuW). Then simply download the latest release: https://github.com/flexerblex/IDM.git and open the project in Android Studio.

You can register for an account using our registration page. After an account is successfully registered, you will recieve an email address with a confirmation. You will then be directed to the login page to login with your password. 

After logging in, you will see a homepage with a test username account (idm_capstone), which is pulling directly from our test account. 

<p float="left">
  <img src="https://github.com/flexerblex/IDM/blob/master/diagrams/login.png?raw=true" width="150" hspace="20">
  <img src="https://github.com/flexerblex/IDM/blob/master/diagrams/home.png?raw=true" width="150">
</p>

You are able to register your chosen form of identification by going to the settings page, which can be found by clicking on the settings button on the homepage. After your identication has been registered, you will be allowed to login to the application with your chosen identification for future use.  

You can edit your profile to:
- Update your username
- Update your password
- Update your email address

<p float="left">
  <img src="https://github.com/flexerblex/IDM/blob/master/diagrams/settings.png?raw=true" width="150" hspace="20">
  <img src="https://github.com/flexerblex/IDM/blob/master/diagrams/edit.png?raw=true" width="150">
</p>

If you have admin access, you will be able to see a list of all users in our database. You will have the option to view more information per user and grant/revoke the following permissions:
* Locking/unlocking accounts
* Granting/revoking permissions to other users

<img src="https://github.com/flexerblex/IDM/blob/master/diagrams/admin.png?raw=true" width="150">

If you have any issues with the application, contact liliasuau@oakland.edu. Any comments and/or suggestions are also welcomed!
