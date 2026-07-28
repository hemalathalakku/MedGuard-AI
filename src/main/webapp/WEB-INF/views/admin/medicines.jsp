<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head><title>Manage Medicines - MedGuard AI</title><%@ include file="/WEB-INF/views/fragments/head.jspf" %></head>
<body class="d-flex flex-column min-vh-100 mg-body">
<%@ include file="/WEB-INF/views/fragments/adminNav.jspf" %>
<div class="container py-4">
  <h4 class="mb-3">Manage Medicines</h4>

  <%-- Add / edit form. In edit mode ${edit} is pre-loaded by the servlet. --%>
  <div class="card mg-glass p-3 mb-4">
    <h6>${empty edit ? 'Add new medicine' : 'Edit medicine'}</h6>
    <form method="post" action="${pageContext.request.contextPath}/admin/medicines" class="row g-2">
      <input type="hidden" name="csrf" value="${sessionScope.csrfToken}">
      <input type="hidden" name="action" value="${empty edit ? 'add' : 'update'}">
      <c:if test="${not empty edit}"><input type="hidden" name="id" value="${edit.medicineId}"></c:if>
      <div class="col-md-3"><input name="medicineName" value="${edit.medicineName}" class="form-control form-control-sm" placeholder="Medicine name *" required></div>
      <div class="col-md-3"><input name="genericName" value="${edit.genericName}" class="form-control form-control-sm" placeholder="Generic name"></div>
      <div class="col-md-3"><input name="brand" value="${edit.brand}" class="form-control form-control-sm" placeholder="Brand"></div>
      <div class="col-md-3"><input name="manufacturer" value="${edit.manufacturer}" class="form-control form-control-sm" placeholder="Manufacturer"></div>
      <div class="col-md-6"><input name="ingredients" value="${edit.ingredients}" class="form-control form-control-sm" placeholder="Ingredients (comma separated - used by allergy checker)"></div>
      <div class="col-md-6"><input name="uses" value="${edit.uses}" class="form-control form-control-sm" placeholder="Uses"></div>
      <div class="col-md-6"><input name="dosage" value="${edit.dosage}" class="form-control form-control-sm" placeholder="Dosage"></div>
      <div class="col-md-6"><input name="sideEffects" value="${edit.sideEffects}" class="form-control form-control-sm" placeholder="Side effects"></div>
      <div class="col-md-6"><input name="contraindications" value="${edit.contraindications}" class="form-control form-control-sm" placeholder="Contraindications"></div>
      <div class="col-md-6"><input name="warnings" value="${edit.warnings}" class="form-control form-control-sm" placeholder="Warnings"></div>
      <div class="col-md-2"><input name="pregnancyCategory" value="${edit.pregnancyCategory}" class="form-control form-control-sm" placeholder="Preg. cat."></div>
      <div class="col-md-4"><input name="storageInstructions" value="${edit.storageInstructions}" class="form-control form-control-sm" placeholder="Storage"></div>
      <div class="col-md-4"><input name="ageRestrictions" value="${edit.ageRestrictions}" class="form-control form-control-sm" placeholder="Age restrictions"></div>
      <div class="col-md-2"><input name="description" value="${edit.description}" class="form-control form-control-sm" placeholder="Description"></div>
      <div class="col-12">
        <button class="btn btn-success btn-sm">${empty edit ? 'Add medicine' : 'Save changes'}</button>
        <c:if test="${not empty edit}">
          <a href="${pageContext.request.contextPath}/admin/medicines" class="btn btn-outline-secondary btn-sm">Cancel</a>
        </c:if>
      </div>
    </form>
  </div>

  <form method="get" action="${pageContext.request.contextPath}/admin/medicines" class="row g-2 mb-3">
    <div class="col-md-5"><input name="q" value="${q}" class="form-control" placeholder="Search medicines..."></div>
    <div class="col-md-2"><button class="btn btn-secondary w-100">Search</button></div>
  </form>

  <div class="table-responsive card mg-glass p-2">
    <table class="table align-middle mb-0">
      <thead><tr><th>#</th><th>Name</th><th>Generic</th><th>Brand</th><th>Manufacturer</th><th></th></tr></thead>
      <tbody>
      <c:forEach var="m" items="${list}">
        <tr>
          <td>${m.medicineId}</td><td class="fw-bold">${m.medicineName}</td>
          <td>${m.genericName}</td><td>${m.brand}</td><td>${m.manufacturer}</td>
          <td class="text-end text-nowrap">
            <a class="btn btn-sm btn-outline-primary"
               href="${pageContext.request.contextPath}/admin/medicines?edit=${m.medicineId}"><i class="bi bi-pencil"></i></a>
            <form method="post" action="${pageContext.request.contextPath}/admin/medicines" class="d-inline"
                  onsubmit="return confirm('Delete ${m.medicineName}?')">
              <input type="hidden" name="csrf" value="${sessionScope.csrfToken}">
              <input type="hidden" name="action" value="delete">
              <input type="hidden" name="id" value="${m.medicineId}">
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
          <a class="page-link" href="${pageContext.request.contextPath}/admin/medicines?q=${q}&page=${p}">${p}</a></li>
      </c:forEach>
    </ul></nav>
  </c:if>
</div>
<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
