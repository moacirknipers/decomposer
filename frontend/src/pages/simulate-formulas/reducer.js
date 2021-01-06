const INITIAL_STATE = {
    variaveis: [],
    formulas: [],
    formulasComposicao: [],
    modalFormulas: [],
    showModal: false,
    result: [],
    tiposCalculo: [],
    selected: '',
    paginacaoModal: { 
        pageNumber: 0,
        totalPages: 0
    }
}

export default (state = INITIAL_STATE, action) => {
    switch (action.type) {
        case 'VARIAVEL_VALUE_CHANGE':
            return { ...state, variaveis: updateVariavel(state.variaveis, action.payload)}
        case 'OPEN_CLOSE_VALUE':
            return { ...state, showModal: action.payload, modalFormulas: []}
        case 'LOAD_FORMULAS':
            return { ...state, formulas: toComboObjectFormulas(action.payload.content) }
        case 'FORMULA_EXECUTION_SUCCESS':
            return { ...state, result: action.payload.output }
        case 'FORMULA_EXECUTION_ERROR':
            return { ...state, result: action.payload }
        case 'LOAD_VARIAVEIS':
            return { ...state, variaveis: action.payload.variaveis, formulasComposicao: action.payload.formulasComposicao, selected: action.selected, showModal: false }
        case 'CLEAR_ALL_VALUES':
            return { ...state, formulasComposicao: [], result: [], selected:'', variaveis: [], showModal: false }
        case 'LOAD_GRUPOS':
            return { ...state, tiposCalculo: toComboObjectGrupos(action.payload) }
        case 'FILTER_MODAL_FORMULAS':
            return { ...state, modalFormulas: action.payload, paginacaoModal: updatePaginacao(action.payload, state.paginacaoModal) }
        default:
            return state
    }
}

//helper
function updateVariavel(variaveis, updated) {
    const index = variaveis.findIndex(item => item.nome === updated.key)
    variaveis[index].value = updated.value
    return variaveis
}

function toComboObjectFormulas(payload) {
    var comboObject = []
    Object.keys(payload).map((key,index) => {
        comboObject.push({
            id: payload[key].id,
            nome: payload[key].nome,
            calculada: payload[key].calculada,
            formula: payload[key].formula,
            tipoVariavel: payload[key].tipoVariavel,
            tipoDado: payload[key].tipoDado,
            descricao: payload[key].descricao,
            tamanho: payload[key].tamanho,
            decimais: payload[key].decimais,
            tipoDeCalculo: payload[key].tipoDeCalculo
        })
    })
    return comboObject
}


function toComboObjectGrupos(payload) {
    var comboObject = []
    Object.keys(payload).map((key,index) => {
        comboObject.push({
            id: payload[key].id,
            nome: payload[key].nome
        })
    })
    return comboObject
}

function updatePaginacao(payload, paginacao) {
    paginacao.pageNumber = payload.number
    paginacao.totalPages = payload.totalPages
    return paginacao
}