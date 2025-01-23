document.getElementById("loginButton").addEventListener("click", async () => {
  // Captura os valores dos campos de usuário e senha
  const username = document.getElementById("formGroupExampleInput").value;
  const password = document.getElementById("formGroupExampleInput2").value;

  // Verifica se os campos não estão vazios
  if (!username || !password) {
    alert("Por favor, preencha todos os campos.");
    return;
  }

  try {
    // Faz a requisição para a API de login
    const response = await fetch("http://localhost:8080/auth/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ name: username, password: password }),
    });

    // Verifica se a resposta foi bem-sucedida
    if (!response.ok) {
      const error = await response.json();
      alert(`Usuário inválido: ${error.message || "Credenciais incorretas."}`);
      return;
    }

    // Obtém o token da resposta
    const data = await response.json();
    const token = data.token;

    // Salva o token no cookie
    document.cookie = `authToken=${token}; path=/; secure; HttpOnly; SameSite=Strict`;

    // Redireciona para a página da câmera
    window.location.href = "http://localhost:8080/camera";
  } catch (error) {
    console.error("Erro ao fazer login:", error);
    alert("Erro ao fazer login.");
  }
});

// Redireciona para a página de cadastro
document
    .getElementById("registerButton")
    .addEventListener("click", function () {
      window.location.href = "/register";
    });
