const quoteText = document.querySelector(".quote");
const authorName = document.querySelector(".author .name");

quoteBtn = document.querySelector("button");
//soundBtn = document.querySelector(".sound");

//Written Quote
const quoteText2 = document.getElementById('quote-text');
const authorText2 = document.getElementById('author-text');

const quotes = [
    {
        quote: "Never Give Up Because You Never Know Tf The Next Try Is Going To Be The One That Works.",
        author: "Fantastic 4"
    },
    {
        quote: "Believe You Can and You're Halfway There.",
        author: "Fantastic 4"
    },
    {
        quote: "The Only Way To Do Great Work Is To Love What You Do.",
        author: "Fantastic 4"
    },
    {
        quote: "The Purpose of Our Lives Is To Be Happy!!!",
        author: "Fantastic 4"
    },
    {
        quote: "Live For Each Second Without Hesitation!!!",
        author: "Fantastic 4"
    },
    {
        quote: "Nothing is impossible. The word itself says I'm possible!",
        author: "Fantastic 4"
    },
    {
        quote: "Every moment is a fresh beginning.",
        author: "Fantastic 4"
    },
    {
        quote: "Never regret anything that made you smile.",
        author: "Fantastic 4"
    },
    {
        quote: "Problems are not stop signs, they are guidelines.",
        author: "Fantastic 4"
    },
    {
        quote: "Life isn't about finding yourself. Life is about creating yourself.",
        author: "Fantastic 4"
    },
    {
        quote: "Mastering others is Strength. Mastering yourself is True Power.",
        author: "Fantastic 4"
    },
    {
        quote: "HAPPINESS is not by chance, but by choice.",
        author: "Fantastic 4"
    },
    {
        quote: "Never Underestimate Yourself, My Friend.",
        author: "Fantastic 4"
    }

];

function getRandomQuote() {
    return quotes[Math.floor(Math.random() * quotes.length)];
}

function displayQuote() {
    const randomQuote = getRandomQuote();
    quoteText2.textContent = randomQuote.quote;
    authorText2.textContent = randomQuote.author;
}

//soundBtn.addEventListener("click", ()=>{
//
//    // Get the available voices
////    let voices = window.speechSynthesis.getVoices();
//
//
//    // Create the utterance with the selected voice
//    let utterance = new SpeechSynthesisUtterance(`${quoteText.innerText} by ${authorName.innerText}`);
//
////    utterance.voice = voices[6];
//     // Speak the utterance
//    speechSynthesis.speak(utterance);
//});

function randomQuote(){

    quoteBtn.classList.add("loading");
    quoteBtn.innerText = "Loading Quote...";

    fetch("https://api.quotable.io/random").then(res => res.json()).then(result =>  {
        console.log(result);
        quoteText.innerText = result.content;
        authorName.innerText = result.author;
        quoteBtn.innerText = "New Quote";
        quoteBtn.classList.remove("loading");

    });
}

quoteBtn.addEventListener("click", randomQuote);
displayQuote();
