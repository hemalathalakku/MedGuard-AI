<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head><title>Medicine Search - MedGuard AI</title><%@ include file="/WEB-INF/views/fragments/head.jspf" %></head>
<body class="d-flex flex-column min-vh-100 mg-body">
<%@ include file="/WEB-INF/views/fragments/userNav.jspf" %>
<div class="container py-4">
  <h4 class="mb-3"><i class="bi bi-search text-primary"></i> Medicine Search</h4>

  <form method="get" action="${pageContext.request.contextPath}/user/medicines" class="row g-2 mb-4 position-relative" autocomplete="off">
    <div class="col-md-8 position-relative">
      <input id="medSearch" name="q" value="${q}" class="form-control"
             placeholder="Search by medicine, brand or generic name...">
      <div id="acBox" class="list-group position-absolute w-100 shadow" style="z-index:1000"></div>
    </div>
    <div class="col-md-2"><button class="btn btn-primary w-100">Search</button></div>
  </form>

  <c:if test="${empty list}"><div class="alert alert-info">No medicines matched "<b>${q}</b>".</div></c:if>

  <div class="row g-3">
    <c:forEach var="m" items="${list}">
      <div class="col-md-6 col-lg-3">
        <div class="card mg-glass h-100 p-3">
          <h6 class="mb-1">${m.medicineName}</h6>
          <div class="small text-muted mb-2">${m.brand} &middot; ${m.genericName}</div>
          <p class="small flex-grow-1">${m.uses}</p>
          <div class="d-flex gap-2">
            <a href="${pageContext.request.contextPath}/user/medicines?id=${m.medicineId}" class="btn btn-sm btn-outline-primary">Details</a>
            <a href="${pageContext.request.contextPath}/user/safety?id=${m.medicineId}" class="btn btn-sm btn-success">Check Safety</a>
          </div>
        </div>
      </div>
    </c:forEach>
  </div>

  <c:if test="${pages > 1}">
    <nav class="mt-4"><ul class="pagination justify-content-center">
      <c:forEach var="p" begin="1" end="${pages}">
        <li class="page-item ${p == page ? 'active' : ''}">
          <a class="page-link" href="${pageContext.request.contextPath}/user/medicines?q=${q}&page=${p}">${p}</a>
        </li>
      </c:forEach>
    </ul></nav>
  </c:if>
</div>
<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
