import React from 'react';

import './styles.css';

const TableVariables = props => {
    const list = props.listVariaveis !== null && props.listVariaveis.totalElements > 0 ? props.listVariaveis.content : []
    const renderList = () => {
        return (
            list.map(data => (
                <div className="table-row" key={data.id}>
                    <div className="table-column" >
                        {data.nome}
                    </div>
                    <div className="table-column">
                        {data.tipoVariavel}
                    </div>
                    <div className="table-column">
                        {data.tipoDado}
                    </div>
                    <div className="table-column">
                        {data.tamanho}
                    </div>
                    <div className="table-column">
                        {data.decimais}
                    </div>
                    <div className="table-column">
                        {data.tipoDeCalculo.nome}
                    </div>
                    <div className="table-column">
                        {data.descricao}
                    </div>
                    <div className="table-column">
                        <div className="btn-area">
                            <button type="button"
                                onClick={() => props.buttonMethod(data.id)}>
                                <i className={props.classButton}></i>
                            </button>
                        </div>
                    </div>
                </div>
            ))
        )
    }
    return (
        <>
        <div className="table mt-2">
            <div className="table-header">
                <div className="header-column">
                    Variável
                    </div>
                <div className="header-column">
                    Tipo
                    </div>
                <div className="header-column">
                    Tipo de Dados
                    </div>
                <div className="header-column">
                    Tamanho
                    </div>
                <div className="header-column">
                    Decimais
                    </div>
                <div className="header-column">
                    Grupo
                    </div>
                <div className="header-column">
                    Descrição
                    </div>
                <div className="header-column">
                    Ação
                    </div>
            </div>
            <div>
                {renderList()}
            </div>
        </div>
        </>
    );
}

export default TableVariables;
