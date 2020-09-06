import json

from browser import ajax, bind, document

# wikicene_url_template = "http://localhost:8080?term={}"
wikicene_url_template = "/?term={}&queryType={}"

default_debug_message = "Search wikipedia articles!"

debug = document["debug"]
query_selector = document["query-selector"]
search_bar = document["search-bar"]
results = document["results"]

result_template = """
    <a href="{page}" class="row justify-content-center text-white mb-3 bg-darker">
        <div class="col col-8">
            <h5 class="mb-2">{title}</h5>
            <p>{summary}</p>
        </div>
        <div class="col col-4">
            <img src="{thumbnail}" alt="thumbnail" class="bg-transparent">
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


def wikicene_request():
    term = search_bar.value
    if term:
        query_type = get_selected_option(query_selector)
        url = wikicene_url_template.format(term, query_type)
        req = ajax.Ajax()
        req.bind("complete", on_wikicine_response)
        req.open("GET", url, True)
        req.send()
    elif results.text:
        results.clear()
        debug.text = default_debug_message


def get_selected_option(selector):
    return selector.options[selector.selectedIndex].value


@bind(search_bar, "input")
def on_text_input(ev):
    wikicene_request()


@bind("select", "change")
def on_query_selector_change(ev):
    wikicene_request()
