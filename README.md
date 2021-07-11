### Notable code files in this repo can be found here:
https://github.com/umz1011/Magic-Card-Collection-App/tree/master/app/src/main/java/com/example/umar1_mdproject_mtg

## Relevant skills I demonstrated in this project:

- Displaying and extracting information from an external REST API in real time using java (https://scryfall.com/docs/api)
- Creating a database and tables within the app using MySQL
- Manipulating the data from the API to store into the users card collection table


## Some Screen Views:

![ScreenViews](https://user-images.githubusercontent.com/22453457/125183624-06569f80-e1e6-11eb-9119-dd3ae483c692.png)


## Snippets of code showcasing use of External REST API (Scryfall):

#### Card Search:

Breakdown of API call:

![image](https://user-images.githubusercontent.com/22453457/125183792-48341580-e1e7-11eb-83ca-91c16a88bec7.png)

User searching a card and establishing connection to API:

![image](https://user-images.githubusercontent.com/22453457/125183964-49197700-e1e8-11eb-82b3-a43ebc40e506.png)

Displaying results of Search with an array:

![image](https://user-images.githubusercontent.com/22453457/125184015-90a00300-e1e8-11eb-8adc-1ff0fa53200e.png)


#### Card View:

After the user searches and taps on a result, the card ID is passed over and used in the bottom URL to make a call to gain all information on that specific card

Example URL:  https://api.scryfall.com/cards/e9d5aee0-5963-41db-a22b-cfea40a967a3 
basically this: https://api.scryfall.com/cards/[CARD+ID]

Below screenshot showcases extracting certain information from the above URL. Some notable extractions are card color, price and image URL:

![image](https://user-images.githubusercontent.com/22453457/125184445-e5914880-e1eb-11eb-8ba2-92063c967e4b.png)

They are notable because the information that was extracted was nested:

![NestedExtractions](https://user-images.githubusercontent.com/22453457/125184424-a236da00-e1eb-11eb-9e0b-bfc5d4ad53bd.png)

---

## Challenges Faced

Implementing the Search was difficult as scryfalls api had limited documentation. I tried many queries but no luck. I then thought maybe the website itself uses the api and I did my desired search there. I took a look at the url and I saw a query similar to the documentation. I took that query and looked at documentation again to backwards engineer it and finally got the Json file of my desired query.

After getting the Search in the listview it was difficult to take the card id from the card when tapped and send it off to the cardview page to be queried for that specific card. So I created a list of IDs after the search was executed, when a user tapped a row it would take the number of the row and use it to find the correct id in the list and send the intent over which would be used to query on cardview.

Getting the image to display was difficult, we tried many different snippets of code with no luck. We then realized that it had to be done with an Async thread. After a long time we finally found some usable code and implemented it to show the image.

Some complications are faced by actions being inputted too quickly by the user. The database interactions can be a little slow to take effect and cause adverse effects (with some crashes) unless given a delay. These could be resolved with disabling UI elements until the completion of the individual transactions. There was not quite enough time to make the process seamlessly operational with our level of experience.

