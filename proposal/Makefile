all:	proposal.pdf

proposal.pdf: proposal.tex references.bib
	pdflatex proposal
	-bibtex proposal
	pdflatex proposal
	pdflatex proposal

quick: 
	pdflatex proposal

clean:
	rm -f *.aux *.bbl *.blg *.log *.dvi *.bak *~ 
