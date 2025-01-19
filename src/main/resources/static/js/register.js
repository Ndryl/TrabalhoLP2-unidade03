document.getElementById("beginButton").addEventListener("click", function () {
  console.log("ok");
  window.location.href = "/begin";
});
document
  .getElementById("registerButton")
  .addEventListener("click", async () => {
    // Captura os valores dos campos
    const username = document.getElementById("formGroupExampleInput").value;
    const email = document.getElementById("formGroupExampleInputEmail").value;
    const password = document.getElementById("formGroupExampleInput2").value;

    // Verifica se os campos não estão vazios
    if (!username || !email || !password) {
      alert("Por favor, preencha todos os campos.");
      return;
    }

    try {
      const response = await fetch("http://localhost:8080/auth/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          name: username,
          email: email,
          password: password,
          role: "ADMIN",
        }),
      });
    
      // Verifica se a resposta foi bem-sucedida
      if (!response.ok) {
        let errorMessage = "Falha no registro";
        if (response.headers.get("Content-Type")?.includes("application/json")) {
          const error = await response.json();
          errorMessage = error.message || errorMessage;
        } else {
          errorMessage = await response.text();
        }
        alert(`Erro: ${errorMessage}`);
        return;
      }
    
      alert("Usuário registrado com sucesso!");
      window.location.href = "/begin";
    } catch (error) {
      console.error("Erro ao registrar usuário:", error);
      alert("Erro ao conectar-se ao servidor.");
    }
  });
