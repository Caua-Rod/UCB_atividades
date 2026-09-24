function go(id){
  document.querySelectorAll('.view').forEach(v => v.classList.toggle('active', v.id === id));
  window.scrollTo(0,0);
  return false;
}
document.addEventListener('click', e => {
  const t = e.target.closest('[data-go]');
  if (t) { e.preventDefault(); go(t.dataset.go); }
});

class Produto {
  constructor(codigo, nome, categoria, marca, preco, estoque, imagem) {
    this.codigo = codigo;
    this.nome = nome;
    this.categoria = categoria;
    this.marca = marca;
    this.preco = preco;
    this.estoque = estoque;
    this.imagem = imagem;
  }
}

const produtos = [
  new Produto(1001, "Ryzen 7 9800X3D", "Processador", "AMD", 1500, 20, "img/ryzen-9800x3d.jpg"),
  new Produto(1002, "Ryzen 9 9950X3D", "Processador", "AMD", 1700, 10, "img/ryzen-9950x3d.jpg"),
  new Produto(1003, "Core Ultra 5 265K", "Processador", "Intel", 1200, 0, "img/core-265k.jpg"),
  new Produto(1004, "Core Ultra 9 285K", "Processador", "Intel", 1300, 40, "img/core-285k.jpg"),
  new Produto(1005, "Ryzen 7 9700X", "Processador", "AMD", 1200, 12, "img/ryzen-9700X.jpg"),
  new Produto(1006, "RTX 5070", "GPU", "Nvidia", 1600, 16, "img/RTX-5070.jpg"),
  new Produto(1007, "RTX 5070 Ti", "GPU", "Nvidia", 1800, 14, "img/RTX-5070ti.jpg"),
  new Produto(1008, "RTX 5080", "GPU", "Nvidia", 1900, 16, "img/RTX-5080.jpg"),
  new Produto(1009, "RX 9060 XT", "GPU", "AMD", 1500, 26, "img/RX-9060.jpg"),
  new Produto(1010, "RX 9070 XT", "GPU", "AMD", 1700, 21, "img/RX-9070.jpg"),
  new Produto(1011, "MAG B650 Tomahawk WiFi", "Mother Board", "MSI", 1100, 11, "img/MAG-B650.jpg"),
  new Produto(1012, "MAG Z890 Tomahawk WiFi", "Mother Board", "MSI", 1200, 18, "img/MAG-Z890.jpg"),
  new Produto(1013, "TUF Gaming X870-Plus WiFi", "Mother Board", "ASUS", 1300, 10, "img/TUF-X870.jpg"),
  new Produto(1014, "DDR5 Fury Beast 16GB", "RAM", "Kingston", 370, 25, "img/RAM16.jpg"),
  new Produto(1015, "DDR5 Fury Beast 32GB", "RAM", "Kingston", 530, 33, "img/RAM32.jpg"),
  new Produto(1016, "SSD Kingston NV2 1TB", "Armazenamento", "Kingston", 420, 23, "img/SSD1T.jpg"),
  new Produto(1017, "SSD Kingston NV2 2TB", "Armazenamento", "Kingston", 650, 29, "img/SSD2T.jpg"),
  new Produto(1018, "Corsair RM750e", "Fonte", "Corsair", 540, 19, "img/RM750e.jpg"),
  new Produto(1019, "Corsair RM850x", "Fonte", "Corsair", 670, 14, "img/RM850x.jpg"),
  new Produto(1020, "MSI MAG A850GL PCIE5", "Fonte", "MSI", 690, 24, "img/A850GL.jpg"),
  new Produto(1021, "ASUS ROG Strix Helios II", "Gabinete", "ASUS", 320, 17, "img/GX601S.jpg"),
  new Produto(1022, "Lian Li O11 Vision", "Gabinete", "Lian Li", 270, 30, "img/O11V.jpg"),
  new Produto(1023, "Corsair 4000D Airflow", "Gabinete", "Corsair", 410, 22, "img/4000D.jpg"),
  new Produto(1024, "Thermalright Peerless Assassin 120 SE", "Cooler", "Thermalright", 260, 32, "img/120-SE.jpg"),
  new Produto(1025, "DeepCool AK620", "Cooler", "DeepCool", 280, 13, "img/AK620.jpg"),
  new Produto(1026, "iCUE H150i Elite Capellix XT", "Cooler", "Corsair", 1000, 23, "img/H150i.jpg"),
  new Produto(1027, "G Pro X Superlight 2", "Periferico", "Logitech", 900, 20, "img/Superlight.jpg"),
  new Produto(1028, "Keychron P1 HE", "Periferico", "Keychron", 1200, 10, "img/P1-HE.jpg"),
  new Produto(1029, "AOC AGON QUAD 27' QHD 180 Hz", "Monitor", "AOC", 1300, 14, "img/AGON.jpg"),
  new Produto(1030, "LG UltraGear OLED 27' 240Hz", "Monitor", "LG", 1600, 19, "img/OLED.jpg")
];

const destaques = document.getElementById("destaques");
const resultados = document.getElementById("resultados");
const todos = document.getElementById("todos");
const CPU = document.getElementById("CPU");
const PV = document.getElementById("PV");
const MB = document.getElementById("MB");
const RAM = document.getElementById("RAM");
const SSD = document.getElementById("SSD");
const FO = document.getElementById("FO");
const GA = document.getElementById("GA");
const CO = document.getElementById("CO");
const PE = document.getElementById("PE");
const MO = document.getElementById("MO");

function criarCard(produto) {
  let disponibilidade;
  let botao;

  if (produto.estoque > 0) {
    disponibilidade = `${produto.estoque} em estoque`;
    botao = `<button class="btn" data-codigo="${produto.codigo}">Adicionar ao carrinho</button>`;
  } else {
    disponibilidade = "Indisponível";
    botao = `<button class="btn" disabled>Indisponível</button>`;
  }

  return `
    <article class="card">
      <div class="thumb"><img src="${produto.imagem}" alt="${produto.nome}"></div>
      <span class="tag">${produto.categoria}</span>
      <h3>${produto.nome}</h3>
      <div class="price">R$ ${produto.preco.toFixed(2)}<small>${disponibilidade}</small></div>
      ${botao}
    </article>`;
}

function exibirProdutos(lista, container) {
  container.innerHTML = "";

  for (let i = 0; i < lista.length; i++) {
    container.innerHTML += criarCard(lista[i]);
  }
}

exibirProdutos(produtos.slice(0, 10), destaques);
exibirProdutos(produtos, resultados);
todos.onclick = () => exibirProdutos(produtos, resultados);
CPU.onclick = () => (exibirProdutos(produtos.slice(0, 5), resultados));
PV.onclick = () => (exibirProdutos(produtos.slice(5, 10), resultados));
MB.onclick = () => (exibirProdutos(produtos.slice(10, 13), resultados));
RAM.onclick = () => (exibirProdutos(produtos.slice(13, 15), resultados));
SSD.onclick = () => (exibirProdutos(produtos.slice(15, 17), resultados));
FO.onclick = () => (exibirProdutos(produtos.slice(17, 20), resultados));
GA.onclick = () => (exibirProdutos(produtos.slice(20, 23), resultados));
CO.onclick = () => (exibirProdutos(produtos.slice(23, 26), resultados));
PE.onclick = () => (exibirProdutos(produtos.slice(26, 28), resultados));
MO.onclick = () => (exibirProdutos(produtos.slice(28, 30), resultados));

class Carrinho {
  constructor() {
    this.itens = [];
  }

  adicionar(produto) {
    for (let i = 0; i < this.itens.length; i++) {
      if (this.itens[i].produto.codigo === produto.codigo) {
        if (this.itens[i].quantidade < produto.estoque) {
          this.itens[i].quantidade++;
          return true;
        }
        return false;
      }
    }

    if (produto.estoque > 0) {
      this.itens.push({ produto: produto, quantidade: 1 });
      return true;
    }

    return false;
  }

  alterarQuantidade(codigo, variacao) {
    for (let i = 0; i < this.itens.length; i++) {
      const item = this.itens[i];

      if (item.produto.codigo === codigo) {
        const novaQuantidade = item.quantidade + variacao;

        if (novaQuantidade > item.produto.estoque) {
          return false;
        }

        if (novaQuantidade < 1) {
          this.remover(codigo);
          return true;
        }

        item.quantidade = novaQuantidade;
        return true;
      }
    }

    return false;
  }

  remover(codigo) {
    this.itens = this.itens.filter(item => item.produto.codigo !== codigo);
  }

  quantidadeTotal() {
    let total = 0;
    for (let i = 0; i < this.itens.length; i++) {
      total += this.itens[i].quantidade;
    }
    return total;
  }

  subtotal() {
    let soma = 0;
    for (let i = 0; i < this.itens.length; i++) {
      soma += this.itens[i].produto.preco * this.itens[i].quantidade;
    }
    return soma;
  }

  desconto() {
    if(this.subtotal() > 1000){
      return 100;
    } else{
      return 0;
    }
  }

  total() {
    return this.subtotal() - this.desconto();
  }

  limpar() {
    while (this.itens.length > 0) {
      this.itens.pop();
    }
  }
}

const carrinho = new Carrinho();

const itensCarrinho = document.getElementById("itens-carrinho");
const contador = document.getElementById("contador-carrinho");
const resumoItens = document.getElementById("resumo-itens");
const resumoSubtotal = document.getElementById("resumo-subtotal");
const resumoDesconto = document.getElementById("resumo-desconto");
const resumoTotal = document.getElementById("resumo-total");

function formatarPreco(valor) {
  return "R$ " + valor.toFixed(2);
}

function atualizarInterface() {
  itensCarrinho.innerHTML = "";

  if (carrinho.itens.length === 0) {
    itensCarrinho.innerHTML = "<p>Seu carrinho está vazio.</p>";
  }

  for (let i = 0; i < carrinho.itens.length; i++) {
    const item = carrinho.itens[i];

    itensCarrinho.innerHTML += `
      <div class="cart-item">
        <div class="thumb"><img src="${item.produto.imagem}" alt="${item.produto.nome}"></div>
        <div>
          <h3>${item.produto.nome}</h3>
          <p class="meta">${formatarPreco(item.produto.preco)}</p>
          <div class="qty">
            <button data-acao="diminuir" data-codigo="${item.produto.codigo}">−</button>
            <span>${item.quantidade}</span>
            <button data-acao="aumentar" data-codigo="${item.produto.codigo}">+</button>
          </div>
        </div>
        <div class="right">
          ${formatarPreco(item.produto.preco * item.quantidade)}<br>
          <button data-acao="remover" data-codigo="${item.produto.codigo}">Remover</button>
        </div>
      </div>
      <br>`;
  }

  contador.textContent = carrinho.quantidadeTotal();
  resumoItens.textContent = carrinho.quantidadeTotal();
  resumoSubtotal.textContent = formatarPreco(carrinho.subtotal());
  resumoDesconto.textContent = formatarPreco(carrinho.desconto());
  resumoTotal.textContent = formatarPreco(carrinho.total());
}

function clicarNoCard(evento) {
  const codigo = Number(evento.target.dataset.codigo);

  if (!codigo) {
    return;
  }

  const produto = produtos.find(item => item.codigo === codigo);

  if (carrinho.adicionar(produto)) {
    atualizarInterface();
  } else {
    alert("Quantidade máxima em estoque atingida.");
  }
}

itensCarrinho.addEventListener("click", function (evento) {
  const acao = evento.target.dataset.acao;
  const codigo = Number(evento.target.dataset.codigo);

  if (!acao) {
    return;
  }

  if (acao === "aumentar") {
    if (!carrinho.alterarQuantidade(codigo, 1)) {
      alert("Quantidade máxima em estoque atingida.");
    }
  } else if (acao === "diminuir") {
    carrinho.alterarQuantidade(codigo, -1);
  } else if (acao === "remover") {
    carrinho.remover(codigo);
  }

  atualizarInterface();
});

destaques.addEventListener("click", clicarNoCard);
resultados.addEventListener("click", clicarNoCard);

atualizarInterface();

const btnFinalizar = document.getElementById("btn-finalizar");
const btnConfirmar = document.getElementById("btn-confirmar");
const listaResumo = document.getElementById("lista-resumo");
const finalItens = document.getElementById("final-itens");
const finalSubtotal = document.getElementById("final-subtotal");
const finalDesconto = document.getElementById("final-desconto");
const finalTotal = document.getElementById("final-total");
const sucessoTotal = document.getElementById("sucesso-total");

function preencherResumo() {
  listaResumo.innerHTML = "";

  for (let i = 0; i < carrinho.itens.length; i++) {
    const item = carrinho.itens[i];

    listaResumo.innerHTML += `
      <div class="cart-item">
        <div class="thumb"><img src="${item.produto.imagem}" alt="${item.produto.nome}"></div>
        <div>
          <h3>${item.produto.nome}</h3>
          <p class="meta">${item.quantidade} x ${formatarPreco(item.produto.preco)}</p>
        </div>
        <div class="right">${formatarPreco(item.produto.preco * item.quantidade)}</div>
      </div>`;
  }

  finalItens.textContent = carrinho.quantidadeTotal();
  finalSubtotal.textContent = formatarPreco(carrinho.subtotal());
  finalDesconto.textContent = formatarPreco(carrinho.desconto());
  finalTotal.textContent = formatarPreco(carrinho.total());
}

function finalizarCompra() {
  if (carrinho.itens.length === 0) {
    alert("Seu carrinho está vazio. Adicione produtos antes de finalizar a compra.");
    return;
  }

  preencherResumo();
  go("resumo");
}

function confirmarCompra() {
  sucessoTotal.textContent = formatarPreco(carrinho.total());
  for (let i = 0; i < carrinho.itens.length; i++) {
    carrinho.itens[i].produto.estoque -= carrinho.itens[i].quantidade;
  }
  carrinho.limpar();
  atualizarInterface();
  go("sucesso");
  exibirProdutos(produtos.slice(0, 10), destaques);
  exibirProdutos(produtos, resultados);
}

btnFinalizar.addEventListener("click", finalizarCompra);
btnConfirmar.addEventListener("click", confirmarCompra);