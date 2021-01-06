import api from './api'

const URL = '/variavel';

export async function listPaginated(size, page) {
    return new Promise((resolve, reject) => {
        api.get(`${URL}?size=${size}&page=${page}`)
        .then(response => {
            return resolve(response.data)
        })
        .catch(error => {
            return reject('Erro ao carregar variáveis')
        })
    })
}

export async function searchByGrupoAndNome(size, page, grupo, nome) {
    return new Promise((resolve, reject) => {
        api.get(`${URL}/find?grupoId=${grupo}&nome=${nome}&size=${size}&page=${page}`)
        .then(response => {
            return resolve(response.data)
        })
        .catch(error => {
            return reject('Erro ao listar variáveis por grupo');
        })
    })
}

export async function save(variavel) {
    return new Promise((resolve, reject) => {
        api.post(URL, variavel).then(response => {
            return resolve(response)
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


export async function validate(formula) {
    return new Promise((resolve, reject) => {
        api.post(`${URL}/validate`, formula).then(response => {
            return resolve(response)
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

export async function update(id, variavel) {
    return new Promise((resolve, reject) => {
        api.put(`${URL}/${id}`, variavel)
        .then(response => {
            return resolve(response.status === 200 ? "Editado com sucesso" : "")
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
    });
}

export async function findById(id) {
    return new Promise((resolve, reject) => {
        api.get(`${URL}/${id}`)
            .then(response => {
                return resolve(response);
            })
            .catch(error => {
                return reject(error.response.data.message);
            })
    });
}