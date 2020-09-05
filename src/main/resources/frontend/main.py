import json

from browser import ajax, bind, document

# wikicene_url_template = "http://localhost:8080?term={}"
wikicene_url_template = "/?term={}"

default_debug_message = "Search wikipedia articles!"

debug = document["debug"]
search_bar = document["search-bar"]
results = document["results"]

result_template = """
    <a href="{page}" class="list-group-item list-group-item-action text-white mb-3 bg-darker">
      <div class="d-flex w-100 justify-content-between">
        <div>
          <h5 class="mb-2">{title}</h5>
          <p>{summary}</p>
        </div>
        <img src="{thumbnail}" alt="eevee" class="img-thumbnail">
      </div>
    </a>
"""


def build_result(article):
    return result_template.format(**article)


def refresh_result_list(articles):
    results.clear()
    results.html = "".join([build_result(article) for article in articles])


def on_wikicine_response(resp):
    raw_data = resp.text
    data = json.loads(raw_data)
    articles = data["articles"]
    debug.text = "Got {} results".format(len(articles))
    refresh_result_list(articles)


def wikicene_request(term):
    url = wikicene_url_template.format(term)
    # debug.text = "Requesting '{}'".format(url)
    req = ajax.Ajax()
    req.bind("complete", on_wikicine_response)
    req.open("GET", url, True)
    req.send()


@bind(search_bar, "input")
def on_text_input(ev):
    term = search_bar.value
    # debug.text = "Input! term={}".format(term)
    if term:
        wikicene_request(term)
    elif results.text:
        results.clear()
        debug.text = default_debug_message
