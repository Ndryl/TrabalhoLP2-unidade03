document.getElementById("beginButton").addEventListener("click", function () {
  console.log("ok");
  window.location.href = "/begin";
});
document
  .getElementById("registerButton")
  .addEventListener("click", async () => {
    // Captura os valores dos campos
    const username = document.getElementById("formGroupExampleInput").value;
    const email = document.getElementById("formGroupExampleInput").value;
    const password = document.getElementById("formGroupExampleInput2").value;

    // Verifica se os campos não estão vazios
    if (!username || !email || !password) {
      alert("Por favor, preencha todos os campos.");
      return;
    }

    try {
      // Faz a requisição para a API de registro
      const response = await fetch("http://localhost:8080/auth/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          name: username,
          email: email,
          password: password,
          role: "ADMIN", // Define a role como ADMIN
        }),
      });

      // Verifica se a resposta foi bem-sucedida
      if (!response.ok) {
        const error = await response.json();
        alert(`Erro: ${error.message || "Falha no registro"}`);
        return;
      }

      alert("Usuário registrado com sucesso!");
      // Redireciona para outra página, se necessário
      window.location.href = "/begin";
    } catch (error) {
      console.error("Erro ao registrar usuário:", error);
      alert("Erro ao conectar-se ao servidor.");
    }
  });
