window.onload = async () => {
    try {
        const response = await fetch("http://localhost:8080/api/products/all");
        const products = await response.json();

        const tbody = document.querySelector("table tbody");
        tbody.innerHTML = ""; // Limpa o conteúdo atual da tabela

        // Adiciona cada produto à tabela
        products.forEach(product => {
            const row = document.createElement("tr");

            row.innerHTML = `
                <td>${product.id}</td>
                <td>${product.name}</td>
                <td>${product.price}</td>
                <td>${product.quantity}</td>
                <td>${product.description}</td>
            `;
            tbody.appendChild(row);
        });
    } catch (error) {
        console.error("Erro ao carregar os produtos:", error);
        alert("Erro ao carregar os produtos.");
    }
};
