#!/usr/bin/env python3
import json
import urllib.request
from time import sleep

dump_file = "random-articles.dump"
url = "https://en.wikipedia.org/api/rest_v1/page/random/summary"
requests_per_second = 0.5
articles_written = 0
failed_requests = 0


def get_json(url):
    return json.load(urllib.request.urlopen(url))


def get_random_article():

    data = get_json(url)

    try:
        article_as_dict = {
            "title": data["title"],
            "summary": data["extract"],
            "description": data["description"],
            "timestamp": data["timestamp"],
            "thumbnail": data["thumbnail"]["source"],
            "page": data["content_urls"]["desktop"]["page"]
        }
    except KeyError:
        print("A field was missing. Requesting another article")
        global failed_requests
        failed_requests += 1
        return get_random_article()

    print("Retrieved article:", article_as_dict["title"])

    return json.dumps(article_as_dict)


def main():

    global articles_written

    print("Requesting {} random articles a second".format(requests_per_second))
    request_interval = 1 / requests_per_second

    with open(dump_file, "a", encoding="UTF-8") as f:

        try:
            while True:
                article = get_random_article()
                f.write(article + "\n")
                articles_written += 1
                sleep(request_interval)
        except KeyboardInterrupt:
            print("Closing file")

    print("Wrote {} files ({} failed requests)".format(
        articles_written,
        failed_requests
    ))


if __name__ == '__main__':
    main()
