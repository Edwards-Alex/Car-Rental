## Car Rental

### Frontend

### Create vite and Install necessary  packages

- `npm create vite@latest` select a framework React and JavaScript

- `npm i react-router-dom` for us navigate page to other page 

- `npm install tailwindcss @tailwindcss/vite` for css  more than https://tailwindcss.com/docs/installation/using-vite

- add the Google font for our project. https://fonts.google.com/  copy code for Font in index.css import and use it in all web page.
  ```css
  @import url('https://fonts.googleapis.com/css2?family=Outfit:wght@100..900&family=Roboto:ital,wght@0,100..900;1,100..900&display=swap');
  
  *{
    font-family: "Outfit", sans-serif;
  }
  ```

- defined some theme color for our website.

  ```css
  @theme{
    --color-primary: #2563EB;
    --color-primary-dull: #1F58D8;
    --color-light: #F1F5F9;
    --color-borderColor: #C4C7D2;
  }
  ```

   



### create the folder structure 

- create `.env` below client to store our environment variables.
- create `components` below src to store our components  use for multiple page.
- create `pages` below src store our pages

### backend



