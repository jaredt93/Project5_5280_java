# Project 5: ITIS 5280
## UNC Charlotte | Advanced Mobile Application Development
### Members:
- Alex Miller
- Tom Va
- Jared Tamulynas

## Link to Youtube Channel
[Project Demo](https://youtu.be/3Ig7cxNcfZQ)

## Project Purpose
In this in class assignment you should work on creating a simple authentication API for mobile application, the requirements are as follows :

In this assignment you will develop a mobile application and any supporting server apis to provide mobile payments. Your app should provide the following features:

The app should use the BrainTree SDK (https://www.braintreepayments.com/ Links to an external site.),
You should use the server SDK and the client SDK for DropIn UI.
You should use the DropIn UI SDK flow with the token and nonce flow.
The app should provide user management features including login, signup and logout.
When a user is created, on your server, the BrainTree API should be contacted to create a user in your server and to contact BrainTree to generate 		a       customerId for the new user.
The customerId received from BrainTree should be stored on your server in order to be used when the user attempts to make future purchases. This customerId     should be used in the BrainTree payment flow in order to enable BrainTree to store the previously used payment methods for a given customerId.
The app provides the user with a list of products to purchase, the user is able to view a product and add it to their shopping cart.  You are provided with     a list of products, pricing and discounts in the provided support file [ZIPDownload ZIP].
The products information should be stored on the server, and should be retrievable through an API. You can simply store the JSON file provided on your          server.
    
 The shopping cart:
    The user is able to view the contents of the shopping cart, and should be able to delete items from the cart.
    If the user adds the same product to the cart then the shopping cart should be updated to reflect the updated quantity of the same product selected.
    The user is able to clear all the shopping cart.
    The user is able to checkout, which should initiate the payment process.
 
 Payment Processing:
    The DropIn UI should be displayed to start the payment process with the use of your server to generate the clientToken.
    The customerId should be provided to BrainTree to enable BrainTree to display the user's previously used payment methods.
    The user should be able to complete the payment process using a new credit card or a previously used credit card.
    The completed transactions should be stored on your server.
    The app should also handle cases of invalid credit cards, use the cards provided at 
        https://developer.paypal.com/braintree/docs/guides/credit-cards/testing-go-live/nodeLinks to an external site. for testing.
 
 Order History:
    The app should enable the user to view their previous orders and the details of each order.
