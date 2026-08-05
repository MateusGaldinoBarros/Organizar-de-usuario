const formEntrar = document.querySelector('#form-entrar');
const campoNome = document.querySelector('#nome');
const mensagem = document.querySelector('#mensagem');
const painelMinhaMaturacao = document.querySelector('#minha-maturacao');
const tituloMinhaMaturacao = document.querySelector('#titulo-minha-maturacao');
const meuTipo = document.querySelector('#meu-tipo');
const meuWhatsapp = document.querySelector('#meu-whatsapp');
const botaoAvancar = document.querySelector('#botao-avancar');
const botaoAtualizar = document.querySelector('#botao-atualizar');
const listaMaturacoes = document.querySelector('#lista-maturacoes');

let usuarioAtual = null;
let atualizacaoEmAndamento = false;

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

function mostrarMinhaMaturacao(usuario) {
    usuarioAtual = usuario;
    tituloMinhaMaturacao.textContent = usuario.nome;
    meuTipo.textContent = usuario.tipoUsuario;
    meuWhatsapp.textContent = usuario.whatsapp;
    painelMinhaMaturacao.hidden = false;
}

function escaparHtml(texto) {
    const elemento = document.createElement('span');
    elemento.textContent = texto;
    return elemento.innerHTML;
}

async function carregarMaturacoes() {
    if (atualizacaoEmAndamento) return;

    atualizacaoEmAndamento = true;
    try {
        const maturacoes = await requisicao('/maturacao/todas');
        const minhaMaturacao = maturacoes.find((usuario) => usuario.id === usuarioAtual?.id);
        if (minhaMaturacao) mostrarMinhaMaturacao(minhaMaturacao);

        if (maturacoes.length === 0) {
            listaMaturacoes.innerHTML = '<p class="vazio">Ainda não há maturações.</p>';
            return;
        }

        listaMaturacoes.innerHTML = maturacoes.map((usuario) => `
            <article class="cartao ${usuario.id === usuarioAtual?.id ? 'atual' : ''}">
                <h3>${escaparHtml(usuario.nome)}</h3>
                <p>${usuario.tipoUsuario}</p>
                <strong>${usuario.whatsapp}</strong>
            </article>
        `).join('');
    } catch (erro) {
        mostrarMensagem(`Não foi possível atualizar as maturações: ${erro.message}`);
    } finally {
        atualizacaoEmAndamento = false;
    }
}

formEntrar.addEventListener('submit', async (evento) => {
    evento.preventDefault();
    const nome = campoNome.value.trim();
    if (!nome) return;

    try {
        const usuario = await requisicao('/maturacao/entrar', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ nome })
        });
        mostrarMinhaMaturacao(usuario);
        mostrarMensagem(`Você entrou na maturação de ${usuario.nome}.`, true);
        await carregarMaturacoes();
    } catch (erro) {
        mostrarMensagem(erro.message);
    }
});

botaoAvancar.addEventListener('click', async () => {
    botaoAvancar.disabled = true;
    try {
        const usuario = await requisicao('/maturacao/avancar', { method: 'POST' });
        mostrarMinhaMaturacao(usuario);
        mostrarMensagem('Maturação avançada.', true);
        await carregarMaturacoes();
    } catch (erro) {
        mostrarMensagem(erro.message);
    } finally {
        botaoAvancar.disabled = false;
    }
});

botaoAtualizar.addEventListener('click', carregarMaturacoes);

async function iniciar() {
    try {
        mostrarMinhaMaturacao(await requisicao('/maturacao'));
    } catch (erro) {
        if (!erro.message.includes('Informe o nome')) mostrarMensagem(erro.message);
    }
    await carregarMaturacoes();
}

iniciar();
window.setInterval(carregarMaturacoes, 5000);
