export function aliPay(data) {
    const div = document.createElement("div");
    div.innerHTML = data;
    document.body.appendChild(div);
    document.forms[0].submit();
}
