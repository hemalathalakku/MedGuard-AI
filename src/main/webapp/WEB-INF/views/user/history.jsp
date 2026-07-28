<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head><title>History - MedGuard AI</title><%@ include file="/WEB-INF/views/fragments/head.jspf" %></head>
<body class="d-flex flex-column min-vh-100 mg-body">
<%@ include file="/WEB-INF/views/fragments/userNav.jspf" %>
<div class="container py-4">
  <div class="d-flex justify-content-between align-items-center mb-3">
    <h4 class="mb-0"><i class="bi bi-clock-history text-secondary"></i> Safety Check History</h4>
    <a class="btn btn-outline-success btn-sm"
       href="${pageContext.request.contextPath}/user/history?export=csv&q=${q}">
      <i class="bi bi-download"></i> Export CSV</a>
  </div>

  <form method="get" action="${pageContext.request.contextPath}/user/history" class="row g-2 mb-3">
    <div class="col-md-6"><input name="q" value="${q}" class="form-control" placeholder="Filter by medicine name..."></div>
    <div class="col-md-2"><button class="btn btn-secondary w-100">Search</button></div>
  </form>

  <c:if test="${empty list}"><div class="alert alert-info">No history yet.</div></c:if>
  <c:if test="${not empty list}">
    <div class="table-responsive card mg-glass p-2">
      <table class="table align-middle mb-0">
        <thead><tr><th>Medicine</th><th>Result</th><th>Warnings</th><th>Checked at</th></tr></thead>
        <tbody>
        <c:forEach var="h" items="${list}">
          <tr>
            <td class="fw-bold">${h.medicineName}</td>
            <td><span class="badge ${h.result == 'SAFE' ? 'text-bg-success' : h.result == 'CAUTION' ? 'text-bg-warning' : 'text-bg-danger'}">${h.result}</span></td>
            <td class="small" style="white-space:pre-line">${h.warnings}</td>
            <td class="small text-muted">${h.checkedAt}</td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </div>
  </c:if>
</div>
<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
