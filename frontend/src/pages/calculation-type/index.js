import React, { useState } from 'react';
import { useHistory } from "react-router-dom";
import { useAlert } from "react-alert";

import api from '@/services/api'
import './styles.css';

const URL = '/tipocalc';

export default function CalculationTypes({ props }) {

    const [nome, setNome] = useState('');

    const alert = useAlert();
    const history = useHistory();

    async function handleClick() {
        history.goBack();
    }

    async function handleRegister(e) {
        e.preventDefault();

        const data = {
            nome
        };

        function clearInputs() {
            setNome('')
        }

        await api.post(URL, data).then(res => {
            alert.success('Cadastro realizado com sucesso!')
            clearInputs();
        }).catch(error => {
            if (error.response.data.errors && error.response.data.errors.length > 0) {
                error.response.data.errors.map(erro => {
                    alert.error(erro.defaultMessage || 'Erro desconhecido')
                })
            } else {
                alert.error(error.response.data.message)
            }
        }
        )
    }

    return (
        <div className="column mt-5 align-items-center">
            <h5>Cadastro de Tipo de Cálculo</h5>
            <hr></hr>
            <form className="ml-3" onSubmit={handleRegister.bind(this)} >
                <div className="row mt-5">
                    <div className="col-md-7">
                        <label>Nome</label>

                        <input
                            id="input"
                            className="input-medium"
                            value={nome}
                            type="text"
                            onChange={e => setNome(e.target.value)} >
                        </input>

                    </div>
                </div>
                <div className="btns-area mt-5">
                    <div className="row">
                        <button className="btn-back" type="button" onClick={handleClick} >Voltar</button>
                        <button className="btn-save" type="submit">Salvar</button>
                    </div>
                </div>
            </form>
            <hr className="mt-5"></hr>
        </div>
    );
}
