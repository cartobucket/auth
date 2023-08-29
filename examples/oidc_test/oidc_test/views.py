from datetime import datetime

from django.contrib.auth.decorators import login_required
from django.http import HttpResponse

@login_required
def home(request):
    now = datetime.now()
    html = f"""
    <html>
    <body>
      <p>It is now {now}.</p>
      <p>You are logged in as: {request.user.email}</p>
      <a href="/logout">Click to logout</a>
    </body>
    </html>"""
    return HttpResponse(html)


def go_to_login(request):
    html = f"""
    <html>
    <body>
      <a href="/">Click to login</a>
      <a href="/logout">Click to logout</a>
    </body>
    </html>"""
    return HttpResponse(html)
