# Conceal

Conceal is a web app that generates word searches, written in ClojureScript.

## Usage

Head over to the [Conceal web app][1] if you just want to try it out (it's hosted on the `gh-pages` branch of this repository).

If you want to hack on it, clone the repository. Navigate to the project directory in a shell and start the auto build:

	$ lein cljsbuild auto

Once it's compiled, start the Ring server:

	$ lein ring server

This will open up localhost:3000 in a web browser automatically. Go to <http://localhost:3000/index.html> to get to the Conceal page. Everything is pretty self-explanatory. A few things to try:

- Enter an invalid input (not a number) in the row or column field.
- Enter words that are too big to possibly fit.
- Print the page (only the word search will show up).

[1]: http://mk12.github.io/conceal/

## License

© 2014 Mitchell Kember

Conceal is available under the MIT License; see [LICENSE](LICENSE.md) for details.