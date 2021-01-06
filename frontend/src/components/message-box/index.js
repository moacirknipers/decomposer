import React from 'react';

import './styles.css'

export default function Alert({ text, type, close }) {

    return (
        <div className={type === 'error' ? ('message alert-error') : ('message alert-success')} id="message">
            <div className={type === 'error' ? ('icon-holder error') : ('icon-holder success')}>
                {type == 'error' ? (<svg xmlns="http://www.w3.org/2000/svg" width="47" height="37" viewBox="0 0 24 24" fill="none" stroke="#ffffff" strokeWidth="2" strokeLinecap="butt" strokeLinejoin="round"><circle cx="12" cy="12" r="10"></circle><line x1="15" y1="9" x2="9" y2="15"></line><line x1="9" y1="9" x2="15" y2="15"></line></svg>) : (<svg xmlns="http://www.w3.org/2000/svg" width="37" height="47" viewBox="0 0 24 24" fill="none" stroke="#ffffff" strokeWidth="2" strokeLinecap="butt" strokeLinejoin="round"><polyline points="20 6 9 17 4 12"></polyline></svg>)}
            </div>
            <div className="text-holder">
                {text}
            </div>
            <button type="button" className="button" onClick={close}>
                <svg xmlns="http://www.w3.org/2000/svg" width="25" height="47" viewBox="0 0 24 24" fill="none" stroke="#ffffff" strokeWidth="2" strokeLinecap="butt" strokeLinejoin="round"><path d="M3 3h18v18H3zM15 9l-6 6m0-6l6 6" /></svg>
            </button>
        </div>
    );
}
