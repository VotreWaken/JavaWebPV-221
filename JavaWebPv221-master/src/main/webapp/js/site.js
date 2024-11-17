document.addEventListener("submit", e => {
    const form = e.target;
    if(form.id === "signup-form") {
        e.preventDefault();
        const formData = new FormData(form);
        fetch(form.action, {
            method: "POST",
            body: formData
        }).then(r => r.json())
            .then(j => {
            console.log(j);
        });
    }
    else if(form.id === "modal-auth-form") {
        e.preventDefault();
        const queryString = new URLSearchParams(new FormData(form)).toString();
        fetch(`${form.action}?${queryString}` ,{
            method: 'PATCH'
        }).then(r => r.json()).then( j => {
            if( j.status === "Ok" ) {
                window.location.reload();
            }
            else {
                console.log( j );
            }
        } );
    }
});

document.addEventListener('DOMContentLoaded', function() {
    // Materialize: init modal(s)
    M.Modal.init(
        document.querySelectorAll('.modal'), {
            opacity: 0.5,
            inDuration:	250,
            outDuration: 250,
            onOpenStart: null,
            onOpenEnd: null,
            onCloseStart: null,
            onCloseEnd:	null,
            preventScrolling: true,
            dismissible: true,
            startingTop: '4%',
            endingTop: '10%',
    });
});