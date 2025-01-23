document.getElementById("loginButton").addEventListener("click", async () => {
  const username = document.getElementById("formGroupExampleInput").value;
  const password = document.getElementById("formGroupExampleInput2").value;

  if (!username || !password) {
    alert("Por favor, preencha todos os campos.");
    return;
  }

  try {
    const response = await fetch("http://localhost:8080/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ name: username, password: password }),
    });

    if (!response.ok) {
      const error = await response.json();
      alert(`Usuário inválido: ${error.message || "Credenciais incorretas."}`);
      return;
    }

    const data = await response.json();
    const token = data.token;

    // Verifique se o token foi retornado
    console.log("Token recebido:", token);
    if (!token) {
      alert("Erro: Token não recebido.");
      return;
    }

    // Salva o token no cookie
    document.cookie = `authToken=${token}; path=/; secure; SameSite=Strict`;

    // Redireciona para a página da câmera
    window.location.href = "/camera";
  } catch (error) {
    console.error("Erro ao fazer login:", error);
    alert("Erro ao fazer login.");
  }
});

document.getElementById("registerButton").addEventListener("click", () => {
  window.location.href = "/register";
});

