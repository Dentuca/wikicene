import json
import time

from browser import ajax, bind, document

# wikicene_url_template = "http://localhost:8080?term={}"
wikicene_url_template = (
    "/?term={term}"
    "&queryType={query_type}"
)

default_debug_message = "Search wikipedia articles!"

debug = document["debug"]
analyzer_selector = document["analyzer-selector"]
field_selector = document["field-selector"]
query_selector = document["query-selector"]
search_bar = document["search-bar"]
results = document["results"]
max_dist_div = document["max-dist-div"]
max_dist_selector = document["max-dist-selector"]

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

start_ts = None


def build_result(article):
    return result_template.format(**article)


def refresh_result_list(articles):
    results.clear()
    results.html = "".join([build_result(article) for article in articles])


def on_wikicine_response(resp):
    latency = int((time.time() - start_ts) * 1000)
    raw_data = resp.text
    data = json.loads(raw_data)
    articles = data["articles"]
    debug.text = (
        "Got {result_count} results in {time} ms"
        " ({latency} ms roundtrip)"
    ).format(
        result_count=len(articles),
        time=data["time"],
        latency=latency
    )
    refresh_result_list(articles)


def wikicene_request():
    term = search_bar.value
    if term:
        global start_ts

        # fetch selected options and build url
        query_type = get_selected_option(query_selector)
        url = wikicene_url_template.format(
            term=term,
            query_type=query_type
        )

        # add the analyzers of the index/store to use
        analyzer_type = get_selected_option(analyzer_selector)
        url += "&storeType={}".format(analyzer_type)

        # add the fields to use when matching (e.g. title, summary)
        for field in get_selected_options(field_selector):
            url += "&queryField={}".format(field)

        # add max edit distance parameter for fuzzy queries
        if query_type == "fuzzy":
            max_edit_dist = get_selected_option(max_dist_selector)
            url += "&maxEdits={}".format(max_edit_dist)

        # create and send an ajax request
        req = ajax.Ajax()
        req.bind("complete", on_wikicine_response)
        req.open("GET", url, True)
        start_ts = time.time()
        req.send()
    elif results.text:
        results.clear()
        debug.text = default_debug_message


def get_selected_option(selector):
    return selector.options[selector.selectedIndex].value


def get_selected_options(selector):
    return [option.value for option in selector if option.selected]


@bind(search_bar, "input")
def on_text_input(ev):
    wikicene_request()


@bind("select", "change")
def on_selector_change(ev):
    wikicene_request()


@bind(query_selector, "change")
def on_query_selector_change(ev):
    if query_selector.value == "fuzzy":
        del max_dist_div.attrs["hidden"]
    elif "hidden" not in max_dist_div:
        max_dist_div.attrs["hidden"] = ""
