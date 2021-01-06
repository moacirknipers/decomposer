import api from './api'

const URL = '/tipocalc';

export async function listCombobox() {
    return new Promise((resolve, reject) => {
        api.get(`${URL}/all`).then(response => {
            return resolve(response.data)
        })
        .catch(error => {
            return reject('Erro ao carregar combobox')
        })
    })
}

