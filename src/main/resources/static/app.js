const formEntrar = document.querySelector('#form-entrar');
const campoNome = document.querySelector('#nome');
const mensagem = document.querySelector('#mensagem');
const painelMinhaRoleta = document.querySelector('#minha-roleta');
const tituloMinhaRoleta = document.querySelector('#titulo-minha-roleta');
const meuTipo = document.querySelector('#meu-tipo');
const meuWhatsapp = document.querySelector('#meu-whatsapp');
const botaoAvancar = document.querySelector('#botao-avancar');
const botaoAtualizar = document.querySelector('#botao-atualizar');
const listaRoletas = document.querySelector('#lista-roletas');

let usuarioAtual = null;

async function requisicao(url, options = {}) {
    const resposta = await fetch(url, options);
    const corpo = await resposta.json().catch(() => null);

    if (!resposta.ok) {
        throw new Error(corpo?.erro || 'Não foi possível concluir a operação.');
    }
    return corpo;
}

function mostrarMensagem(texto, sucesso = false) {
    mensagem.textContent = texto;
    mensagem.classList.toggle('sucesso', sucesso);
}

function mostrarMinhaRoleta(usuario) {
    usuarioAtual = usuario;
    tituloMinhaRoleta.textContent = usuario.nome;
    meuTipo.textContent = usuario.tipoUsuario;
    meuWhatsapp.textContent = usuario.whatsapp;
    painelMinhaRoleta.hidden = false;
}

function escaparHtml(texto) {
    const elemento = document.createElement('span');
    elemento.textContent = texto;
    return elemento.innerHTML;
}

async function carregarRoletas() {
    try {
        const roletas = await requisicao('/roleta/todas');
        if (roletas.length === 0) {
            listaRoletas.innerHTML = '<p class="vazio">Ainda não há roletas.</p>';
            return;
        }

        listaRoletas.innerHTML = roletas.map((usuario) => `
            <article class="cartao ${usuario.id === usuarioAtual?.id ? 'atual' : ''}">
                <h3>${escaparHtml(usuario.nome)}</h3>
                <p>${usuario.tipoUsuario}</p>
                <strong>${usuario.whatsapp}</strong>
            </article>
        `).join('');
    } catch (erro) {
        listaRoletas.innerHTML = `<p class="vazio">${escaparHtml(erro.message)}</p>`;
    }
}

formEntrar.addEventListener('submit', async (evento) => {
    evento.preventDefault();
    const nome = campoNome.value.trim();
    if (!nome) return;

    try {
        const usuario = await requisicao('/roleta/entrar', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ nome })
        });
        mostrarMinhaRoleta(usuario);
        mostrarMensagem(`Você entrou na roleta de ${usuario.nome}.`, true);
        await carregarRoletas();
    } catch (erro) {
        mostrarMensagem(erro.message);
    }
});

botaoAvancar.addEventListener('click', async () => {
    botaoAvancar.disabled = true;
    try {
        const usuario = await requisicao('/roleta/avancar', { method: 'POST' });
        mostrarMinhaRoleta(usuario);
        mostrarMensagem('Roleta avançada.', true);
        await carregarRoletas();
    } catch (erro) {
        mostrarMensagem(erro.message);
    } finally {
        botaoAvancar.disabled = false;
    }
});

botaoAtualizar.addEventListener('click', carregarRoletas);

async function iniciar() {
    try {
        mostrarMinhaRoleta(await requisicao('/roleta'));
    } catch (erro) {
        if (!erro.message.includes('Informe o nome')) mostrarMensagem(erro.message);
    }
    await carregarRoletas();
}

iniciar();
