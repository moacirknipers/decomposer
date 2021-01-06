import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';

import Paginator from '@/components/paginator';
import ComboBox from '@/components/combobox';
import TableVariables from '@/components/table-variables';

import { listVariables, loadGrupos } from './actions'

import './styles.css';

class ListVariables extends Component {
    constructor(props) {
        super(props)
        this.state = {searchDescricao: '', searchGrupo: ''}
    }

    componentWillMount() {
        this.props.listVariables(null, null, null)
        this.props.loadGrupos()  
    }

    searchDescricaoChange(event) {
        this.setState({searchDescricao: event.target.value})
      }

    searchGrupoChange(event) {
        this.setState({searchGrupo: event.target.value})
      }  

    sendSearch = (page) => {
        var filter = []
        var localPage = 0
        if (page !== null) {
            localPage = page
        } 
        var size = 5
        filter.descricao = this.state.searchDescricao
        filter.tipoDeCalculo = this.state.searchGrupo
        this.props.listVariables(filter, localPage, size)
    }

    chamaCadastro = () => {
        if (this.props.history !== null) {
            this.props.history.push('/')
        }
    }

    editarItem = (value) => {
        if (this.props.history !== null) {
            this.props.history.push(`/variables/${value}`)
        }
    }

    render() {
        return (
            <div>            
            <div className="column mt-5">
                <h5>Listagem de Configuração de Variáveis de Cálculo</h5>
                <hr></hr>
                <div className="row">
                    <div className="search-description col-md-5">
                        <label>Descrição</label>
                        <input className="modalDescricao" type="text" value={this.state.searchDescricao} onChange={this.searchDescricaoChange.bind(this)}/>
                    </div>
                    <ComboBox size="col-md-4" descricao="Grupo" options={this.props.tiposCalculo} value={this.state.searchGrupo} onChangeCalculo={this.searchGrupoChange.bind(this)} />
                    <div className="btn-save-area">
                        <button onClick={() => this.sendSearch(0)}>+ Pesquisar</button>
                    </div>
                    <div className="btn-save-area">
                        <button onClick={() => this.chamaCadastro()}>+ Cadastrar</button>
                    </div>
    
                </div>
                <TableVariables listVariaveis={this.props.variaveis} buttonMethod={this.editarItem} classButton="fa fa-edit" />
                <Paginator pageOptions={this.props.paginacao} changePage={this.sendSearch}/>
            </div>
            </div>
        )
    }
}

const mapStateToProps = state => ({
    variaveis: state.listVars.variaveis,
    tiposCalculo: state.simulate.tiposCalculo,
    paginacao: state.listVars.paginacao
})

const mapDispatchToProps = dispatch => bindActionCreators({ listVariables, loadGrupos }, dispatch)

export default connect(mapStateToProps, mapDispatchToProps)(ListVariables)