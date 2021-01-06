import React, { useState, useEffect } from 'react';
import { useHistory } from "react-router-dom";
import { useAlert } from "react-alert";

import api from '@/services/api'

import './styles.css';
import TableVariables from '@/components/table-variables';
import Paginator from '../../components/paginator/index';
import { searchByGrupoAndNome, save, validate, update, findById } from '@/services/variavelService';

const URL = '/tipocalc';

export default function ManageVariables({ match }) {

    const alert = useAlert();
    const history = useHistory();
    const [types, setTypes] = useState([]);
    const [listVariables, setListVariables] = useState([]);
    const [search, setSearch] = useState({grupo: '', nome: ''});

    const PAGE_OPTIONS_INITIAL_STATE = {
        pageSize: 5,
        pageNumber: 0,
        totalPages: 0
    }
    const [pageOptions, setPageOptions] = useState(PAGE_OPTIONS_INITIAL_STATE);

    const VARIABLE_INITIAL_STATE = {
        nome: '',
        formula: '',
        tipoVariavel: '',
        tipoDado: '',
        descricao: '',
        tamanho: 0,
        decimais: 0
    }

    const [variable, setVariable] = useState([VARIABLE_INITIAL_STATE]);

    useEffect(() => {
        findAllTypes();
        if (match.params.variableId !== undefined) {
            findVariavelById(match.params.variableId);
        }
    }, [])


    function findVariavelById(id) {
        findById(id).then((response) => {
            setVariable({
                nome: response.data.nome, tipoVariavel: response.data.tipoVariavel,
                tamanho: response.data.tamanho, decimais: response.data.decimais, descricao: response.data.descricao, tipoDeCalculoId: response.data.tipoDeCalculoId,
                tipoDado: response.data.tipoDado, formula: response.data.formula
            })
        })
    }

    function goBack() {
        history.goBack();
    }

    async function findAllTypes() {
        if (match.params.variableId !== undefined) {

        }
        await api.get(`${URL}/all`).then((res) => {
            setTypes(res.data);
        })
    }

    async function searchVariables() {
        searchByGrupoAndNome(pageOptions.pageSize, pageOptions.pageNumber, search.grupo, search.nome).then(response => {
            setListVariables(response.content)
            setPageOptions({ ...pageOptions, totalPages: response.totalPages, pageNumber: response.pageable.pageNumber, pageSize: response.pageable.pageSize })
        })
    }

    async function validateFormula() {
        validate(variable).then(response => {
            alert.success(response.data)
        }).catch(error => {
            treatError(error);
        })
    }

    function clearInputs() {
        let allInputs = document.getElementsByTagName('input');
        let allSelect = document.getElementsByTagName('select');
        for (let i = 0; i < allInputs.length; i++) {
            allInputs[i].value = '';
        }

        for (let i = 0; i < allSelect.length; i++) {
            allSelect[i].selectedIndex = 0;
        }
    }

    function treatError(error) {
        if (error instanceof Array) {
            error.map(erro => {
                alert.error(erro)
            })
        } else {
            alert.error(error)
        }
    }

    function handleUpdate() {
        update(match.params.variableId, {
            nome: variable.nome, tipoVariavel: variable.tipoVariavel, tamanho: variable.tamanho,
            decimais: variable.decimais, descricao: variable.descricao, tipoDeCalculoId: variable.tipoDeCalculoId,
            tipoDado: variable.tipoDado,
            formula: variable.formula
        }).then((response) => {
            alert.success(response);
        }).catch(error => {
            treatError(error);
        })
    }

    function addVariableToTextArea(variableToBeAdded) {
        if(variable.formula != undefined)
            setVariable({...variable, formula: variable.formula.concat(" ").concat(variableToBeAdded)})
        else
            setVariable({...variable, formula: variableToBeAdded})
    }

    async function createVariable(event) {
        event.preventDefault();
        if (match.params.variableId !== undefined) {
            return handleUpdate();
        }

        save(variable).then(response => {
            alert.success('Fórmula cadastrada com sucesso');
            clearInputs();
            setVariable([VARIABLE_INITIAL_STATE]);

        }).catch(error => {
            treatError(error);
        })
    }

    return (
        <div className="column mt-2 align-items-center" onSubmit={createVariable}>
            <h5>Configuração Variávies de Cálculo</h5>
            <hr></hr>
            <form className="ml-3" >
                <div className="row mt-3">
                    <div className="col-md-4">
                        <label>Variável</label>
                        <input
                            id="variavel"
                            className="input-medium"
                            value={variable.nome}
                            type="text"
                            onChange={e =>
                                setVariable({
                                    ...variable, nome: e.target.value.replace(/[' ']/g, '')
                                })}
                        />
                    </div>
                    <div className="col-md-2 mb-3">
                        <label>Tipo de Variável</label>
                        <select value={variable.tipoVariavel} onChange={e => setVariable({ ...variable, tipoVariavel: e.target.value })}>
                            <option>Selecione uma opção</option>
                            <option value="CALCULO">Cálculo</option>
                            <option value="CAMPO">Campo</option>
                        </select>
                    </div>
                    <div className="col-md-2">
                        <label>Tipo de Dado</label>
                        <select value={variable.tipoDado} onChange={e => setVariable({ ...variable, tipoDado: e.target.value })}>
                            <option>Selecione uma opção</option>
                            <option value="INTEIRO">Inteiro</option>
                            <option value="DECIMAL">Decimal</option>
                            {variable.tipoVariavel === 'CALCULO' ? (null) : (<option value="CARACTER">Texto</option>)}
                        </select>
                    </div>
                    <div className="col-md-1">
                        <label>Tamanho</label>
                        <input className="input-medium" value={variable.tamanho} type="number" onChange={e => setVariable({ ...variable, tamanho: +e.target.value })}></input>
                    </div>
                    <div className="col-md-1">
                        <label>Decimais</label>
                        <input disabled={variable.tipoDado !== 'DECIMAL'} value={variable.tipoDado !== "DECIMAL" ? variable.decimais = '' : variable.decimais} className="input-medium" type="number" max="9" onChange={e => setVariable({ ...variable, decimais: e.target.value })}></input>
                    </div>
                    <div className="col-md-6">
                        <label>Descrição</label>
                        <input className="input-medium" value={variable.descricao} type="text" onChange={e => setVariable({ ...variable, descricao: e.target.value })}></input>
                    </div>
                    <div className="col-md-4">
                        <label>Grupo de Variáveis</label>
                        <select value={variable.tipoDeCalculoId} onChange={e => setVariable({ ...variable, tipoDeCalculoId: e.target.value })}>
                            <option>Selecione uma opção</option>
                            {types.map(type => (
                                <option key={type.id} value={type.id}>{type.nome}</option>
                            ))}
                        </select>
                    </div>
                    <button type="button" className="btn-add" onClick={() => history.push('/calc')}>
                        <i className="fa fa-plus"></i>
                    </button>
                    <div className="col-md-11 mt-1">
                        <h6>Fórmula</h6>
                        <textarea placeholder="Insira a fórmula aqui..."
                            id="formulaInput" value={variable.formula} onChange={e => setVariable({ ...variable, formula: e.target.value })}
                            disabled={variable.tipoVariavel === 'CAMPO'}
                        >
                        </textarea>
                    </div>
                </div>
                <div className="btns-area mt-1">
                    <div className="row">
                        <button className="btn-back" type="button" onClick={goBack}>Voltar</button>
                        <button className="btn-execute" type="button" onClick={validateFormula}>Validar</button>
                        <button className="btn-save" type="submit">Salvar</button>
                    </div>
                </div>
            </form>
            <div className="row w-100">
                <div className="col-md-3">
                    <label>Grupo de Variáveis</label>
                    <select onChange={e => setSearch({...search, grupo: e.target.value})}>
                        <option>Selecione uma opção</option>
                        {types.map(type => (
                            <option key={type.id} value={type.id}>{type.nome}</option>
                        ))}
                    </select>
            </div>
            <div className="col-md-3">
                <label>Variável</label>
                <input type="text" onChange={e => setSearch({...search, nome: e.target.value})}></input>
            </div>
            <button type="button" className="btn-search" onClick={searchVariables}>Pesquisar</button>
        </div>
        <TableVariables listVariaveis={listVariables} buttonMethod={addVariableToTextArea} classButton="fa fa-plus" />
        <Paginator pageOptions={pageOptions} changePage={search} />
        <hr className="mt-5"></hr>
        </div >
    );
}
