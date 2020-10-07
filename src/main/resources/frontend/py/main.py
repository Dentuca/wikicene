import json
import time

from browser import ajax, bind, document

wikicene_url_template = (
    "/?term={term}"
    "&analyzerType={analyzer_type}"
    "&queryType={query_type}"
)

# analyzis options
analyzer_selector = document["analyzer-selector"]
tokenizer_selector = document["tokenizer-selector"]
token_filter_selector = document["token-filter-selector"]

# query options
field_selector = document["field-selector"]
query_selector = document["query-selector"]
max_dist_selector = document["max-dist-selector"]

# hidden divs
tokenizer_div = document["tokenizer-div"]
token_filter_div = document["token-filter-div"]
max_dist_div = document["max-dist-div"]

# search bar
search_bar = document["search-bar"]

# text label below search bar
debug = document["debug"]
default_debug_message = "Search wikipedia articles!"

# results
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

    if "error" in data:
        results.clear()
        debug.text = "Server error ({status_code}): {message}".format(
            status_code=data["error"],
            message=data["message"]
        )
    else:
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
        analyzer_type = get_selected_option(analyzer_selector)
        query_type = get_selected_option(query_selector)
        url = wikicene_url_template.format(
            term=term,
            analyzer_type=analyzer_type,
            query_type=query_type
        )

        if analyzer_type == "custom":

            # add the tokenizer of the custom analyzer
            tokenizer = get_selected_option(tokenizer_selector)
            url += "&tokenizer={}".format(tokenizer)

            # add token filters
            for token_filter in get_selected_options(token_filter_selector):
                url += "&tokenFilter={}".format(token_filter)

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


@bind(analyzer_selector, "change")
def on_analyzer_selector_change(ev):
    if analyzer_selector.value == "custom":
        del tokenizer_div.attrs["hidden"]
        del token_filter_div.attrs["hidden"]
    else:
        if "hidden" not in tokenizer_div:
            tokenizer_div.attrs["hidden"] = ""
        if "hidden" not in token_filter_div:
            token_filter_div.attrs["hidden"] = ""


@bind(query_selector, "change")
def on_query_selector_change(ev):
    if query_selector.value == "fuzzy":
        del max_dist_div.attrs["hidden"]
    elif "hidden" not in max_dist_div:
        max_dist_div.attrs["hidden"] = ""
