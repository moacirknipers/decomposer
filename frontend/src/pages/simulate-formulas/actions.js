import api from '@/services/api'

export const variavelValueChange = event => ({
    type: 'VARIAVEL_VALUE_CHANGE',
    payload: {
        key: event.target.id,
        value: event.target.value
    }
    
})

export const openCloseModal = value => ({
    type: 'OPEN_CLOSE_VALUE',
    payload: value
})

export const loadVariaveis = (id) => {
    return dispatch => {
        api.get(`/variavel/${id}/variaveis`).then(
            resp => dispatch({
                type: 'LOAD_VARIAVEIS',
                payload: resp.data,
                selected: id
            })
        )
    }
}

export const loadFormulas = () => {
    return dispatch => {
        api.get(`/variavel/list?tipoVariavel=CALCULO`).then(
            resp => dispatch({
                type: 'LOAD_FORMULAS',
                payload: resp.data
            })
        )
    }
}

export const filterModalFormulas = (filters) => {
    var size = 5
    var page = 0
    var descricao = ""
    var tipoDeCalculo = ""
    if (filters !== null) {
        size = filters.size !== null ? filters.size : 5
        page = filters.page !== null ? filters.page : 0  
        descricao = filters.descricao
        tipoDeCalculo = filters.tipoDeCalculo
    }

    return dispatch => {
        api.get(`/variavel/list?descricao=${descricao}&tipoDeCalculo=${tipoDeCalculo}&tipoVariavel=CALCULO&page=${page}&size=${size}`).then(
            resp => dispatch({
                type: 'FILTER_MODAL_FORMULAS',
                payload: resp.data
            })
        )
    }
}

export const execute = (idFormula, formulas, variaveis) => {
    const index = formulas.findIndex(item => item.id === idFormula)
    var formula = formulas[index]
    var payload = dictionaryToArray(formula, variaveis)
    console.log(variaveis)
    return dispatch => {
        api.post(`/calcular`, payload)
        .then(resp => dispatch({
            type: 'FORMULA_EXECUTION_SUCCESS',
            payload: resp.data
        }))
        .catch(error => dispatch({
            type: 'FORMULA_EXECUTION_ERROR',
            payload: error
        }))
    }
}

export const searchFormula = (descricao, grupo) => {
    return dispatch => {
        api.post(`/search-formula`, {descricao, grupo})
        .then(resp => dispatch({
            type: 'SEARCH_FORMULA_SUCCESS',
            payload: resp.data
        }))
        .catch(error => dispatch({
            type: 'SEARCH_FORMULA_SUCCESS',
            payload: error
        }))
    }
}

export const clearAll = () => ({
    type: 'CLEAR_ALL_VALUES'    
})

export const clearModalFormulas = () => ({
    type: 'CLEAR_MODAL_FORMULAS'    
})

export const loadGrupos = () => {
    return dispatch => {
        api.get(`/tipocalc/all`).then(
            resp => dispatch({
                type: 'LOAD_GRUPOS',
                payload: resp.data
            })
        )
    }
}

const dictionaryToArray = (formula, variaveis) => {
    var params = []
    Object.keys(variaveis).map((key,index) => {
        params.push({
            key: variaveis[key].nome,
            value: ((variaveis[key].tipoDado === 'DECIMAL') || (variaveis[key].tipoDado === 'INTEIRO')) ? Number(variaveis[key].value) : variaveis[key].value
        })
    })

    return { variavel: formula, params }
}