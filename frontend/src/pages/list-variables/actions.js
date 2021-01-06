import { list } from '@/services/listService'
import api from '@/services/api'

export const listVariables = (filters, page, size) => {
    var localSize = (size !== null) ? size : 5
    var localPage = (page !== null) ? page : 0
    return dispatch => {
        list(localSize, localPage, filters)
            .then(response => {
                dispatch({
                    type: 'LOAD_VARIABLES',
                    payload: response.data
                })
            }).catch(error => {
                dispatch({
                    type: 'ERROR_LOAD_VARIABLES',
                    payload: error
                })
            })
    }
}

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