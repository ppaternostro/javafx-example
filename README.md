# javafx-example
A JavaFX CRUD (Create, Read, Update, Delete) GUI (Graphical User Interface) application.

This application provides CRUD capability utilizing the public [JSON Placeholder](https://jsonplaceholder.typicode.com/) API and displays the results in a JavaFx application. The JSON Placeholder **post** resource is used to invoke the APIs via Java's [HttpClient](https://openjdk.org/groups/net/httpclient/intro.html) introduced in Java 11.

To run the application enter the below from a terminal window.

> mvnw javafx:run (use **./mvnw** for Unix/Linux based OSes)

![Initial](https://github.com/user-attachments/assets/c692244d-a026-4131-a2ac-0f73df99efad)

Searches are performed by numeric id or by the default which will retrieve all post objects when clicking on the **Search** button when **All** is selected from the **Search By** drop down list.

![Search By All](https://github.com/user-attachments/assets/51d050a9-8828-49d4-8084-7b5114f68626)

All the table columns are sortable by clicking their respective column headers. Continuously clicking the column header cycles the sort direction. Note the upside down triangle icon in the **Id** column below.

![Id Column Sort](https://github.com/user-attachments/assets/3fd84e04-5df6-43dd-92c3-62f1a7ec8a1a)

The **Delete** and **Create** buttons are only enabled if a table row is selected. 

![Row Selected](https://github.com/user-attachments/assets/fa8a74fa-64f6-43ab-9fc5-060e92fccbae)

The JSON Placeholder API's **create** (POST), **update** (PUT) and **delete** (DELETE) operations are faked and will not persist changes on the server side. The returned responses will reflect results as if the operations were actually persisted server side.

The only editable columns in the result table are the **Title** and **Body** columns. To initiate an edit, double click inside those column values on the selected row. Double clicking the column value will change the column into an editable text field. Make the desired change and press enter to execute the PUT API and persist the change in the table's column.

![Edit Column](https://github.com/user-attachments/assets/d3052de8-3d04-4834-9b31-ffa99a111779)

Clicking on the **Create** button will display a data entry dialog box for creating a new post. The created post will be associated with the user id value of the currently selected row. The dialog's **User Id** text field will be pre-filled and disabled.

![Create Post Dialog](https://github.com/user-attachments/assets/8a676bc7-bbc6-4135-88b2-14a54a143a31)

Since creating a new post is faked by the JSON Placeholder API, creating multiple posts produces the same post id for each newly created post. This isn't representative of an actual API call and as such sorting the result table produces anomalous results on those newly created rows with the same id. I believe this is related to the table's backing **ObservableList** interface and the unique id assigned to each **Post** object via the class *hashCode* method. Since the same id (and subsequently same hash code) is generated for new posts this is most likely causing the anomalous results.

Another issue uncovered with the newly created posts occurs when editing the editable column (**Title** and **Body**) values . Editing those column values produces a *500 Internal Server Error* response code when calling the PUT API. This issue was not listed in the JSON Placeholder API's [known issues](https://github.com/typicode/jsonplaceholder/issues) but I was able to reproduce the error when using [Postman](https://www.postman.com/) to execute the PUT API with a post id value of *101* or greater. The below stack trace was produced in the Postman console window.

>TypeError: Cannot read properties of undefined (reading 'id')
>at update (/app/node_modules/json-server/lib/server/router/plural.js:262:24)
>at Layer.handle [as handle_request] (/app/node_modules/express/lib/router/layer.js:95:5)
>at next (/app/node_modules/express/lib/router/route.js:137:13)
>at next (/app/node_modules/express/lib/router/route.js:131:14)
>at Route.dispatch (/app/node_modules/express/lib/router/route.js:112:3)
>at Layer.handle [as handle_request] (/app/node_modules/express/lib/router/layer.js:95:5)
>at /app/node_modules/express/lib/router/index.js:281:22
>at param (/app/node_modules/express/lib/router/index.js:354:14)
>at param (/app/node_modules/express/lib/router/index.js:365:14)
>at Function.process_params (/app/node_modules/express/lib/router/index.js:410:3)

Despite the issues stemming from using the free, fake JSON Placeholder API, this application is illustrative of using JavaFx as your GUI.

# Resources

- [Getting Started with JavaFx](https://openjfx.io/openjfx-docs/#introduction)
- [Scene Builder](https://gluonhq.com/products/scene-builder)
- [Learn JavaFx Programming](https://www.youtube.com/playlist?list=PL3bGLnkkGnuUQemxQ3ojPcuMt5_d8B5py)
