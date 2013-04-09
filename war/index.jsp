<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<!DOCTYPE html>

<meta charset="utf-8">
<title>Google Cloud Storage Sample Uploader</title>
<link rel="stylesheet" href="/css/reset.css">
<link rel="stylesheet" href="/css/index.css">

<div id="container">

<h1>Google Cloud Storage Sample Uploader</h1>

<ul id="errors">
  <c:forEach var="e" items="${f:errors()}">
    <li>${f:h(e)}</li>
  </c:forEach>
</ul>

<form id="uploadForm" action="/upload" method="post" enctype="multipart/form-data">
  <p>アップロード</p>
  <p>file: <input type="file" name="file"></p>
  <p>title: <input type="text" name="title" value="${title}"></p>
  <p class="button"><input type="submit" value="POST"></p>
</form>

<div id="images">
  <c:forEach var="image" items="${images}">
    <div class="image">
      <a href="/show?id=${image.id}"><img src="${image.thumbnailUrl}=s148"></a>
      <p class="title">${f:h(image.title)}</p>
    </div>
  </c:forEach>
</div>

</div>