<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head><title>Manage Diseases - MedGuard AI</title><%@ include file="/WEB-INF/views/fragments/head.jspf" %></head>
<body class="d-flex flex-column min-vh-100 mg-body">
<%@ include file="/WEB-INF/views/fragments/adminNav.jspf" %>
<div class="container py-4" style="max-width:820px">
  <h4 class="mb-3">Manage Diseases</h4>

  <div class="card mg-glass p-3 mb-4">
    <form method="post" action="${pageContext.request.contextPath}/admin/diseases" class="row g-2">
      <input type="hidden" name="csrf" value="${sessionScope.csrfToken}">
      <div class="col-md-4"><input name="name" class="form-control form-control-sm" placeholder="Disease name *" required></div>
      <div class="col-md-6"><input name="description" class="form-control form-control-sm" placeholder="Short description"></div>
      <div class="col-md-2"><button class="btn btn-danger btn-sm w-100">Add</button></div>
    </form>
  </div>

  <div class="table-responsive card mg-glass p-2">
    <table class="table align-middle mb-0">
      <thead><tr><th>#</th><th>Disease</th><th>Description</th><th></th></tr></thead>
      <tbody>
      <c:forEach var="d" items="${diseases}">
        <tr>
          <td>${d.diseaseId}</td><td class="fw-bold">${d.diseaseName}</td>
          <td class="small text-muted">${d.description}</td>
          <td class="text-end">
            <form method="post" action="${pageContext.request.contextPath}/admin/diseases" class="d-inline"
                  onsubmit="return confirm('Delete ${d.diseaseName}?')">
              <input type="hidden" name="csrf" value="${sessionScope.csrfToken}">
              <input type="hidden" name="action" value="delete">
              <input type="hidden" name="id" value="${d.diseaseId}">
              <button class="btn btn-sm btn-outline-danger"><i class="bi bi-trash"></i></button>
            </form>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>
</div>
<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
