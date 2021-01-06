import React, { useState } from 'react';
import { Link } from 'react-router-dom';

import './styles.css'

export default function Sidebar() {

    const [isOpened, setIsOpened] = useState(false);

    var linkStyle = {
        textDecoration: 'none',
        color: 'inherit',
        width: '100%'
    }

    function openSide() {
        if (isOpened) {
            document.getElementById('main').style.gridTemplateColumns = '90px 12fr';
            setIsOpened(false);
            return
        }
        document.getElementById('main').style.gridTemplateColumns = '200px 12fr';
        setIsOpened(true);
    }

    function closeSide() {
        document.getElementById('main').style.gridTemplateColumns = '90px 12fr';
        setIsOpened(false);
    }

    return (
        <div className="sidenav">
            <div className="side-title">
                <button className="title-btn" onClick={openSide}>
                    {!isOpened ? (<i className="fa fa-align-justify fa-2x"></i>) : (<button className="title-btn" onClick={closeSide}><h3>POC</h3></button>)}
                </button>
            </div>
            <Link to="/list" style={linkStyle}>
                <div className="sidenav-item">
                    <i className="fa fa-superscript fa-lg"></i>
                    {isOpened ? (<p className="item-description">Configurar Variáveis</p>) : null}
                </div>
            </Link>
            <Link to="/simulate-formulas" style={linkStyle}>
                <div className="sidenav-item">
                    <i className="fa fa-calculator fa-lg"></i>
                    {isOpened ? (<p className="item-description">Simular cálculo</p>) : null}
                </div>
            </Link>
        </div>
    );
}
