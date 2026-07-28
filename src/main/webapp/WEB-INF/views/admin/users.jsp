<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head><title>Manage Users - MedGuard AI</title><%@ include file="/WEB-INF/views/fragments/head.jspf" %></head>
<body class="d-flex flex-column min-vh-100 mg-body">
<%@ include file="/WEB-INF/views/fragments/adminNav.jspf" %>
<div class="container py-4">
  <h4 class="mb-3">Manage Users</h4>

  <form method="get" action="${pageContext.request.contextPath}/admin/users" class="row g-2 mb-3">
    <div class="col-md-5"><input name="q" value="${q}" class="form-control" placeholder="Search name or email..."></div>
    <div class="col-md-2"><button class="btn btn-secondary w-100">Search</button></div>
  </form>

  <div class="table-responsive card mg-glass p-2">
    <table class="table align-middle mb-0">
      <thead><tr><th>#</th><th>Name</th><th>Email</th><th>Age</th><th>Gender</th><th>Blood</th><th></th></tr></thead>
      <tbody>
      <c:forEach var="u" items="${users}">
        <tr>
          <td>${u.userId}</td><td class="fw-bold">${u.fullName}</td><td>${u.email}</td>
          <td>${u.age}</td><td>${u.gender}</td><td>${u.bloodGroup}</td>
          <td class="text-end">
            <form method="post" action="${pageContext.request.contextPath}/admin/users" class="d-inline"
                  onsubmit="return confirm('Delete user ${u.fullName}? All their data is removed.')">
              <input type="hidden" name="csrf" value="${sessionScope.csrfToken}">
              <input type="hidden" name="action" value="delete">
              <input type="hidden" name="id" value="${u.userId}">
              <button class="btn btn-sm btn-outline-danger"><i class="bi bi-trash"></i></button>
            </form>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>

  <c:if test="${pages > 1}">
    <nav class="mt-3"><ul class="pagination justify-content-center">
      <c:forEach var="p" begin="1" end="${pages}">
        <li class="page-item ${p == page ? 'active' : ''}">
          <a class="page-link" href="${pageContext.request.contextPath}/admin/users?q=${q}&page=${p}">${p}</a></li>
      </c:forEach>
    </ul></nav>
  </c:if>
</div>
<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
