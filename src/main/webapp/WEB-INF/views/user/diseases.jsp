<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head><title>Health Profile - MedGuard AI</title><%@ include file="/WEB-INF/views/fragments/head.jspf" %></head>
<body class="d-flex flex-column min-vh-100 mg-body">
<%@ include file="/WEB-INF/views/fragments/userNav.jspf" %>
<div class="container py-4">
  <h4 class="mb-3"><i class="bi bi-heart-pulse text-danger"></i> Health Profile</h4>
  <div class="row g-4">

    <div class="col-lg-6">
      <div class="card mg-glass p-3 h-100">
        <h6>My diseases</h6>
        <form method="post" action="${pageContext.request.contextPath}/user/diseases" class="row g-2 mb-3">
          <input type="hidden" name="csrf" value="${sessionScope.csrfToken}">
          <input type="hidden" name="action" value="addDisease">
          <div class="col-6">
            <select name="diseaseId" class="form-select form-select-sm" required>
              <option value="">Choose disease...</option>
              <c:forEach var="d" items="${allDiseases}"><option value="${d.diseaseId}">${d.diseaseName}</option></c:forEach>
            </select>
          </div>
          <div class="col-4"><input type="date" name="diagnosedOn" class="form-control form-control-sm" title="Diagnosed on"></div>
          <div class="col-2"><button class="btn btn-sm btn-danger w-100">Add</button></div>
        </form>
        <table class="table table-sm align-middle">
          <thead><tr><th>Disease</th><th>Diagnosed</th><th></th></tr></thead>
          <tbody>
          <c:forEach var="d" items="${myDiseases}">
            <tr>
              <td>${d.diseaseName}</td>
              <td class="text-muted small">${d.diagnosedOn}</td>
              <td class="text-end">
                <form method="post" action="${pageContext.request.contextPath}/user/diseases" class="d-inline">
                  <input type="hidden" name="csrf" value="${sessionScope.csrfToken}">
                  <input type="hidden" name="action" value="removeDisease">
                  <input type="hidden" name="diseaseId" value="${d.diseaseId}">
                  <button class="btn btn-sm btn-outline-danger" title="Remove"><i class="bi bi-x"></i></button>
                </form>
              </td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </div>
    </div>

    <div class="col-lg-6">
      <div class="card mg-glass p-3 h-100">
        <h6>My allergies</h6>
        <form method="post" action="${pageContext.request.contextPath}/user/diseases" class="row g-2 mb-3">
          <input type="hidden" name="csrf" value="${sessionScope.csrfToken}">
          <input type="hidden" name="action" value="addAllergy">
          <div class="col-9"><input name="allergyName" class="form-control form-control-sm" placeholder="e.g. Penicillin" required></div>
          <div class="col-3"><button class="btn btn-sm btn-warning w-100">Add</button></div>
        </form>
        <ul class="list-group list-group-flush">
          <c:forEach var="a" items="${myAllergies}">
            <li class="list-group-item bg-transparent d-flex justify-content-between align-items-center">
              ${a.value}
              <form method="post" action="${pageContext.request.contextPath}/user/diseases" class="d-inline">
                <input type="hidden" name="csrf" value="${sessionScope.csrfToken}">
                <input type="hidden" name="action" value="removeAllergy">
                <input type="hidden" name="allergyId" value="${a.key}">
                <button class="btn btn-sm btn-outline-danger"><i class="bi bi-x"></i></button>
              </form>
            </li>
          </c:forEach>
        </ul>
      </div>
    </div>
  </div>
</div>
<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
