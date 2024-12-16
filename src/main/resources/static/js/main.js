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
      alert(`Erro: ${error.message || "Falha no login"}`);
      return;
    }
    console.log("ok");

    // Obtém o token da resposta
    const data = await response.json();
    const token = data.token;

    // Salva o token no cookie
    document.cookie = `authToken=${token}; path=/; secure; HttpOnly; SameSite=Strict`;

    // Exibe mensagem de sucesso ou redireciona o usuário
    alert("Login bem-sucedido!");
    // Redirecionar para outra página, se necessário
    // window.location.href = "/dashboard";
  } catch (error) {
    console.error("Erro ao fazer login:", error);
    alert("Erro ao conectar-se ao servidor.");
  }
});
document
  .getElementById("registerButton")
  .addEventListener("click", function () {
    window.location.href = "/register";
  });
