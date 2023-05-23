import requests


if __name__ == '__main__':
    url = 'http://5.254.67.18:5001/viseu/api/imagenet'
    files = {'file' : open('./n01440764_tench.JPEG', 'rb')}
    r = requests.post(url, files=files)
    print(r.content)