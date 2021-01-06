const INITIAL_STATE = {
    variaveis: [],
    tiposCalculo: [],
    paginacao: {
        pageNumber: 0,
        totalPages: 0
    }
}

export default (state = INITIAL_STATE, action) => {
    switch (action.type) {
        case 'LOAD_VARIABLES':
            return {...state, variaveis: action.payload, paginacao: updatePaginacao(action.payload, state.paginacao)}
        case 'LOAD_GRUPOS':
            return { ...state, tiposCalculo: toComboObjectGrupos(action.payload) }            
        default:
            return state
    }

}

//helpers

function updatePaginacao(payload, paginacao) {
    paginacao.pageNumber = payload.number
    paginacao.totalPages = payload.totalPages
    return paginacao
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