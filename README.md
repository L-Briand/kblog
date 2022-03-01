# ![kblog Logo](logo.png) KBlog

A simple blog software using kotlin ktor with markdown and templating. It uses only `.md` files and static files for
content.

## How to build

You need to have a working `1.8+` jre environment. In root directory :

```bash
./gradlew build
# or in windows
.\gradlew.bat build
```

Build release package is inside `./kblog/build/distributions`

## How to use

First download kblog software. Either by [the release page](https://github.com/L-Briand/kblog/releases) or by building
it yourself via the section above. You also need to have a working `1.8+` jre environment. (`java -version` should yield
something.)

Extract the package :

```bash
# with tar 
tar -xf ./kblog/build/distributions/kblog-0.0.1.tar

# with zip
unzip ./kblog/build/distributions/kblog-0.0.1.zip
```

Then run it : `./kblog-0.0.1/bin/kblog`. (A `kblog.bat` exists for windows.)

## How does it works

### Content folder

By default, it uses the sub-folder `blog` at where the program is launched as a root directory for content delivery. If
you have cloned this repository you should have a `blog` folder at root with minimal content just for
show. [You can take a look by yourself.](./blog)

Inside the `blog` folder lies two sub-folder :

- `templates` It holds templates for **markdown** documents to be rendered as html. Since it's a markdown blog it only
  search for `.md` files. The default page template is `md.html`. All other extensions ending with `.md` are also
  considered as template ready. If a file is named `my_article.funky.md` the software will search for the
  `funky.md.html` template.
- `public` It contains all publicly available content and documents. Available through `http://localhost:8080/public/`

### Documents render

All files ending with `.md` inside the `public` folder are considered "documents" and are directly accessible as root
page. Example : `public/My article.md` will show as `http://localhost:8080/My-article`.

Default index file is `index.md` and if a document isn't found it defaults to `404.md`.

If file contains diacritics, the name is automatically freed from them. Example : `./public/Suis-je étonné ?.art.md`
will appear as `http://localhost:8080/Suis-je-etonne`. Available chars are `[a-z], [A-Z], [0-9], -, _` and it trims
trailing `-` automatically.

> Note: You can have some undefined behavior if two articles collapse to the same name or have different extension.

### Arguments

| Argument        | default | Description                  | example                   |
|-----------------|---------|------------------------------|---------------------------|
| -d, --directory | blog    | Set content delivery folder  | `kblog -d my/custom/path` |
| -p, --port      | 8080    | Set port                     | `kblog -p 80`             |
| -l, --listen    | 0.0.0.0 | Defaults listen connections  | `kblog -l 127.0.0.1`      |
