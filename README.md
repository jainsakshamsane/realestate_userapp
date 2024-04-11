I developed a userapp application named "RealEstate User" utilizing the Firebase Realtime Database with user signup-login.

Screen (1,2) - Login/Sign up into your Account
Gives you option of Login and Signup with the funtionality of ensuring that only unique email and phone number can be entered only by the user along with image and city.

Main Activity -
It comes to main activity where you get five fragments.

Fragment 1 - Home Fragement
This fragment displays all categories and its details, along with the popular nearest properties of user's city, search bar to search any property by name and one menu image to see properties according to sale type.
1.) Categories Page - this page displays particular property of that category only.
2.) Saletype Page - this page displays particular property of that saletype only.
1.) More Details Page - this page displays complete detail of property along with multiple images as added by the broker, like/dislike functionality, booknow button to proceed further.
2.) Broker Details Page - this page displays broker details of that property, chatbutton to chat with broker and booknow button to proceed to payment.
3.) Checkout Page - this page displays property name, broker name and price and button to complete payment. once completed, it takes you to main activity.

Fragment 2 - Favourites Fragment
this fragment displays all properties which are marked as "liked" by the user.

Fragment 3 - History Fragment
this fragment displays all properties whose payment completed by the user (along with broker name, price and time).

Fragment 4  - Chat Fragement
this fragment displays latest chats of the user and broker's properties (if did chat).

Fragment 5 - Profile Fragment
this fragment displays all details of user, along with logout functionality, edit profile and get verify yourself button (if not verified).
1.) Edit Profile Page - this page displays all details of user and can edit and save anything.
2.) OTP Verify Page - this page lets you generate OTP, re-send it, and on entering correct OTP only will verify you.

All these functionalities are there along with some eye-catching UI designs, texts, animations, to make the user experince more practical, attractive and user-friendly.
