import api from './api'

const URL = '/variavel';


export async function save(variable) {
    return new Promise((resolve, reject) => {
        api.post(URL, variable)
            .then(response => {
                return resolve(response.status === 200 ? "Salvo com sucesso" : "")
            })
            .catch(error => {
                if (error.response.data.errors && error.response.data.errors.length > 0) {
                    var msgErros = []
                    error.response.data.errors.map(erro => {
                        msgErros.push(erro.defaultMessage || 'Erro desconhecido')
                    })

                    return reject(msgErros)
                } else {
                    return reject(error.response.data.message)
                }
            })
    })
}

export async function list(size, page, filters) {
    var descricao = ""
    var tipoDeCalculo = ""
    if (filters !== null) {
        descricao = filters.descricao
        tipoDeCalculo = filters.tipoDeCalculo
    }
    return new Promise((resolve, reject) => {
        api.get(`${URL}/list?page=${page}&size=${size}&descricao=${descricao}&tipoDeCalculo=${tipoDeCalculo}`)
        .then(response => {
            return resolve(response)
        })
        .catch(error => {
            return reject('Erro ao carregar lista variáveis')
        })
    })
}



